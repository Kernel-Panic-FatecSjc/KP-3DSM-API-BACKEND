package com.kernelpanic.api_gateway.config;

import com.kernelpanic.api_gateway.filtros.AutenticacaoFiltro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


import java.util.Arrays;

@Configuration
public class GatewayConfig {

        @Autowired
        private AutenticacaoFiltro authFilter;

        /// TODO: Adicionar mais rotas e filtros conforme necessário
        ///
        /// TODO: Colocar o EUREKA para funcionar.
        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
                return builder.routes()
                                // Rota de Usuários: PROTEGIDA
                                .route("user-service", r -> r
                                                .path("/usuario/**")
                                                .filters(f -> f.filter(authFilter.apply(c -> c
                                                                .setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN")))))
                                                .uri("http://localhost:8083"))

                                // Rota de Projetos: PROTEGIDA
                                .route("project-service", r -> r
                                                .path("/projeto/**")
                                                //.filters(f -> f.filter(authFilter.apply(new AutenticacaoFiltro.Config())))
                                                .uri("http://localhost:8082"))

                                // Rota de Autenticação (Login): PÚBLICA
                                .route("auth-service", r -> r
                                                .path("/auth/**")
                                                .uri("http://localhost:8081"))

                                .build();
                // .route("apontamento dehoras", r -> r
                // .path("/auth/**")
                // .uri("http://localhost:8084"))

                // .build();

        }

        @Bean
        public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); 
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}