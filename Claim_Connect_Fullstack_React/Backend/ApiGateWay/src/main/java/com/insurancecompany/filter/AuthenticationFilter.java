package com.insurancecompany.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.insurancecompany.utils.JwtUtils;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DataBuffer;
import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
	private RouteValidator validator;

	@Autowired
	private JwtUtils util;

	public static class Config {
	}

	public AuthenticationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest serverHttpRequest = null;
			if (validator.isSecured.test(exchange.getRequest())) {
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					return handleUnauthorized(exchange, "Missing authorization header");
				}

				String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7);
				}

				try {
					Long userId = util.extractUserId(authHeader);
					String role = util.extractRolesFromToken(authHeader);
					String requestedPath = exchange.getRequest().getPath().toString();
					String method = exchange.getRequest().getMethod().name();

					if (!isAuthorized(role, requestedPath, method)) {
						return handleUnauthorized(exchange, "Unauthorized access");
					}
					
					// Add userId to request headers as a string
                    serverHttpRequest = exchange.getRequest().mutate().header("userId", String.valueOf(userId)).build();

				} catch (Exception e) {
					return handleUnauthorized(exchange, "Invalid token");
				}
			}
			System.out.println(exchange);
			return chain.filter(exchange.mutate().request(serverHttpRequest).build());
		};
	}

	private boolean isAuthorized(String role, String path, String method) {
		// Authorization logic based on role, path, and method
		if ("Hospital".equalsIgnoreCase(role)) {
			return hospitalAccessPaths(path, method);
		} else if ("Patient".equalsIgnoreCase(role)) {
			return patientAccessPaths(path, method);
		} else if ("Insurance".equalsIgnoreCase(role)) {
			return insuranceAccessPaths(path, method);
		}
		return false;
	}

	private boolean hospitalAccessPaths(String path, String method) {
		return path.startsWith("/api/hospital") || path.startsWith("/api/claims") || (path.startsWith("/api/patient/getPatientById") && method.equalsIgnoreCase("GET"));
	}

	private boolean patientAccessPaths(String path, String method) {
		return 	path.startsWith("/api/patient")
				|| path.startsWith("/api/claims");
	}
	
	private boolean insuranceAccessPaths(String path, String method) {
		return 	path.startsWith("/api/insuranceComp")
				|| path.startsWith("/api/claims");
	}

	private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {

		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.FORBIDDEN);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

		String jsonResponse = "{\"error\": \"" + message + "\"}";
		DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));

		return response.writeWith(Mono.just(buffer));
	}
}
