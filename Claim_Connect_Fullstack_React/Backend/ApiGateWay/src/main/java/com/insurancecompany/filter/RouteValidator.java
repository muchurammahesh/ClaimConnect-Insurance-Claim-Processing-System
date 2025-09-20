package com.insurancecompany.filter;

import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {


	public static final String[] OPEN_API_ENDPOINTS = { "/api/hospital/authenticate","/api/hospital/login", "/api/hospital/signup", "/api/insuranceComp/signup", "/api/insuranceComp/authenticate", "/api/insuranceComp/getAllICs","/api/patient/getPatientById", "/api/patient/signup","/api/patient/authenticate", "/eureka" };

	public Predicate<ServerHttpRequest> isSecured = request -> {
		String path = request.getPath().toString();
		for (String endpoint : OPEN_API_ENDPOINTS) {
			if (path.contains(endpoint)) {
				return false; // Endpoint does not require authorization
			}
		}
		return true; // Endpoint requires authorization
	};

	
	

}
