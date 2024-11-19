package org.aston.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import static org.aston.apigateway.configuration.ApplicationConstant.*;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routerValidator;
    private final JwtUtil jwtUtil;

    public AuthenticationFilter(RouteValidator routerValidator, JwtUtil jwtUtil) {
        super(Config.class);
        this.routerValidator = routerValidator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (routerValidator.isSecured.test(request)) {
                if (isAuthMissing(request)) return onError(exchange, "Токен доступа отсутствует в запросе");
                if (isPrefixMissing(request)) return onError(exchange, ("Неверный префикс токена доступа"));
                try {
                    String token = getAuthHeader(request);
                    Claims claims = jwtUtil.validateToken(token).getPayload();

                    ServerHttpRequest modifiedRequest = request.mutate()
                            .headers(httpHeaders -> httpHeaders.remove(HttpHeaders.AUTHORIZATION))
                            .header(HEADER_KEY_CUSTOMER_PHONE, jwtUtil.extractCustomerPhone(claims))
                            .header(HEADER_KEY_CUSTOMER_ID, jwtUtil.extractCustomerId(claims))
                            .build();

                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                } catch (SignatureException ex) {
                    String errMessage = "Невалидный токен - неверная подпись токена";
                    log.error(errMessage, ex);
                    return onError(exchange, (errMessage));
                } catch (ExpiredJwtException ex) {
                    Date expiration = ex.getClaims().getExpiration();
                    String errMessage = "Срок хранения токена истек: " + expiration;
                    log.error(errMessage, ex);
                    return onError(exchange, (errMessage));
                }
            }
            return chain.filter(exchange);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err) {
        ServerHttpResponse response = exchange.getResponse();
        byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = new DefaultDataBufferFactory().wrap(bytes);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(Mono.just(buffer));
    }

    private String getAuthHeader(ServerHttpRequest request) {
        List<String> authorization = request.getHeaders().getOrEmpty(AUTH_HEADER);
        String header = authorization.stream().findFirst().orElseThrow();
        return header.substring(7);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(AUTH_HEADER);
    }

    private boolean isPrefixMissing(ServerHttpRequest request) {
        var header = request.getHeaders().getFirst(AUTH_HEADER);
        assert header != null;
        return !header.startsWith(TOKEN_PREFIX);
    }

    public static class Config {
    }
}
