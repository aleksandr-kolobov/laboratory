package org.aston.apigateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    public static final List<String> openApiEndpoints =
            List.of("api/v1/customer/auth",
                    "api/v1/customer/registry",
                    "api/v1/auth/register",
                    "api/v1/card-service/card-product/list_card_products");
}
