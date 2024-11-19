package org.aston.apigateway.security.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.aston.apigateway.security.AuthenticationFilter;
import org.aston.apigateway.security.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import java.time.Duration;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.aston.apigateway.data.AuthTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT, classes = AuthenticationFilterTest.AuthenticationFilterTestConfig.class,
        properties = {"spring.cloud.config.enabled=false", "spring.cloud.discovery.enabled=false"})
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
class AuthenticationFilterTest {
    private String token;
    @Value("${jwt.jwt-expiration}")
    private Duration jwtExpiration;
    @Value("${jwt.secret}")
    private String secret;

    @BeforeAll
    public void init() {
        token = createToken(PHONE_NUMBER, secret, jwtExpiration.toMillis());
        wireMockServer.stubFor(WireMock.get(urlPathMatching("/api/v1/auth/test"))
                .willReturn(WireMock.ok()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                )
        );
    }

    @TestConfiguration
    static class AuthenticationFilterTestConfig {
        @Autowired
        AuthenticationFilter authenticationFilter;

        @Bean(destroyMethod = "stop")
        WireMockServer wireMockServer() {
            WireMockConfiguration options = wireMockConfig().dynamicPort();
            WireMockServer wireMock = new WireMockServer(options);
            wireMock.start();
            return wireMock;
        }

        @Bean
        RouteLocator testRoutes(RouteLocatorBuilder builder, WireMockServer wireMock) {
            AuthenticationFilter.Config config = new AuthenticationFilter.Config();
            GatewayFilter gatewayFilter = authenticationFilter.apply(config);
            return builder
                    .routes()
                    .route(predicateSpec -> predicateSpec
                            .path("/api/v1/auth/register")
                            .filters(spec -> spec.filter(gatewayFilter))
                            .uri(wireMock.baseUrl()))
                    .route(predicateSpec -> predicateSpec
                            .path("/api/v1/auth/test")
                            .filters(spec -> spec.filter(gatewayFilter))
                            .uri(wireMock.baseUrl()))
                    .build();
        }
    }

    @SpyBean
    JwtUtil jwtUtil;
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    WireMockServer wireMockServer;

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }

    @Test
    void testWireMock() {
        assertTrue(wireMockServer.isRunning());
    }

    @Test
    void testApply_thenDonNotValidateTokenAndReturnIsOk() {
        wireMockServer.stubFor(WireMock.get(urlPathMatching("/api/v1/auth/register"))
                .willReturn(WireMock.ok()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                )
        );
        webTestClient
                .get()
                .uri("/api/v1/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/auth/register")));
        Mockito.verify(jwtUtil, times(0)).validateToken(token);
    }

    @Test
    void testApply_thenValidateTokenAndReturnIsOk() {
        webTestClient.get().uri("/api/v1/auth/test")
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token)
                .exchange()
                .expectStatus().isOk();
        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/auth/test")));
        Mockito.verify(jwtUtil, times(1)).validateToken(token);

    }

    @Test
    void testApply_thenReturnNotAuthorizedAndWrongPrefixMsg() {
        Flux<String> responseBody = webTestClient.get().uri("/api/v1/auth/test")
                .header(HttpHeaders.AUTHORIZATION, "WRONG_PREFIX" + token)
                .exchange()
                .expectStatus().isUnauthorized()
                .returnResult(String.class).getResponseBody();

        wireMockServer.verify(0, getRequestedFor(urlEqualTo("/api/v1/auth/test")));
        responseBody.subscribe(s -> assertEquals(s, "Неверный префикс токена доступа"));
    }

    @Test
    void testApply_thenReturnNotAuthorizedAndAbsentTokenMsg() {
        Flux<String> responseBody = webTestClient.get().uri("/api/v1/auth/test")
                .header("WRONG_HEADER", TOKEN_PREFIX + token)
                .exchange()
                .expectStatus().isUnauthorized()
                .returnResult(String.class).getResponseBody();

        wireMockServer.verify(0, getRequestedFor(urlEqualTo("/api/v1/auth/test")));
        responseBody.subscribe(s -> assertEquals(s, "Токен доступа отсутствует в запросе"));
    }

    @Test
    void testApply_thenReturnNotAuthorizedAndNonValidTokenMsg() {
        Flux<String> responseBody = webTestClient.get().uri("/api/v1/auth/test")
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN_WRONG_SIGNATURE)
                .exchange()
                .expectStatus().isUnauthorized()
                .returnResult(String.class).getResponseBody();

        wireMockServer.verify(0, getRequestedFor(urlEqualTo("/api/v1/auth/test")));
        responseBody.subscribe(s -> assertEquals(s, "Невалидный токен - неверная подпись токена"));
    }

    @Test
    void testApply_thenReturnNotAuthorizedAndTokenExpiredMsg() {
        Flux<String> responseBody = webTestClient.get().uri("/api/v1/auth/test")
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN_EXPIRED_OR_REPEATED)
                .exchange()
                .expectStatus().isUnauthorized()
                .returnResult(String.class).getResponseBody();

        wireMockServer.verify(0, getRequestedFor(urlEqualTo("/api/v1/auth/test")));
        responseBody.subscribe(s -> assertTrue(s.contains("Срок хранения токена истек: ")));

    }
}
