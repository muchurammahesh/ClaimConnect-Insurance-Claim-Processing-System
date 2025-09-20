package com.insurancecompany.utils;

import java.security.Key;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtils {


    public static final String SECRET = "d3780ec3d1cfaba271e0538d4fae686d8367e10155ee424691fbf191eabec53d";

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }
    
    public Long extractUserId(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }
    

    public String extractRolesFromToken(final String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
       
        String authorities = (String) claims.get("roles"); // Assuming roles are stored as a List<String> in "roles" claim
      
        // Convert authorities to roles in the format "ROLE_ADMIN" or "ROLE_USER"

        return authorities;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
