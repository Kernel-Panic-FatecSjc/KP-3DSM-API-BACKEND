package com.kernelpanic.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/usuarios/**")
                        .uri("http://localhost:8081"))
                .route("project-service", r -> r
                        .path("/projects/**")
                        .uri("http://localhost:8082"))
                .build();
    }
}
