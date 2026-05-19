package com.kernelpanic.api_gateway.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.kernelpanic.api_gateway.filtros.AutenticacaoFiltro;

@Configuration
public class GatewayConfig {

        @Autowired
        private AutenticacaoFiltro authFilter;

        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
                return builder.routes()
                                .route("user-service", r -> r
                                                .path("/usuario/**")
                                                .uri("http://host.docker.internal:8083"))
                                .route("project-service", r -> r
                                                .path("/projeto/**")
                                                .uri("http://host.docker.internal:8082"))
                                .route("auth-service", r -> r
                                                .path("/auth/**")
                                                .uri("http://host.docker.internal:8081"))
                                .route("apotamento-horas", r -> r
                                                .path("/horas/**")
                                                .uri("http://host.docker.internal:8084"))
                                .route("task-service", r -> r.path("/tarefas/**").uri("http://host.docker.internal:8085")).build();
        }

        @Bean
        public CorsWebFilter corsWebFilter() {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                config.setAllowedHeaders(Arrays.asList("*"));
                config.setAllowCredentials(true);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return new CorsWebFilter(source);
        }
}