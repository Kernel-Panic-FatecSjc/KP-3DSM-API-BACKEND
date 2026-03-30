package com.kernelpanic.api_gateway.filtros;

import com.kernelpanic.api_gateway.utils.JwtUtils;
import io.jsonwebtoken.Claims;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AutenticacaoFiltro extends AbstractGatewayFilterFactory<AutenticacaoFiltro.Config> {

    @Autowired
    private JwtUtils jwtUtils;

    public AutenticacaoFiltro() {
        super(Config.class);
    }

    public static class Config {
        private List<String> roles; // Agora é uma lista!
        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles; }
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsHeader(HttpHeaders.AUTHORIZATION)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token não fornecido");
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Formato de token inválido");
            }

            String token = authHeader.substring(7);

            try {
                Claims claims = jwtUtils.getClaims(token);
                String userRole = claims.get("cargo", String.class);


                if (config.getRoles() != null && !config.getRoles().isEmpty()) {

                    if (!config.getRoles().contains(userRole)) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cargo não autorizado para acessar este recurso");
                    }
                }

                ServerHttpRequest requestMutada = exchange.getRequest().mutate()
                        .header("X-User-Id", claims.getSubject())
                        .header("X-User-Role", userRole)
                        .header("X-User-Email", claims.get("email", String.class))
                        .build();

                return chain.filter(exchange.mutate().request(requestMutada).build());

            } catch (ResponseStatusException e) { // Captura o 403 que jogamos acima
                throw e;
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado");
            }
        };
    }
}