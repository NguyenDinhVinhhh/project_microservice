package vti.api_gateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import vti.api_gateway.exceptions.ValidationException;
import vti.api_gateway.services.JwtService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {
    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";
    private static final int TOKEN_INDEX = 7;

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (this.isAuthMissing(request)) {
            throw new ValidationException(HttpStatus.UNAUTHORIZED, "Authorization header is missing in request");
        }

        String authHeader = this.getAuthHeader(request);
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(TOKEN_PREFIX)) {
            throw new ValidationException(HttpStatus.UNAUTHORIZED, "Authorization header method is incorrect");
        }

        String token = authHeader.substring(TOKEN_INDEX);
        String userName = jwtService.extractUsername(token);

        if (!jwtService.validateToken(token, userName)) {
            throw new ValidationException(HttpStatus.UNAUTHORIZED, "Token invalid");
        }

        return chain.filter(exchange);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(AUTH_HEADER_KEY);
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty(AUTH_HEADER_KEY).get(0);
    }

}
