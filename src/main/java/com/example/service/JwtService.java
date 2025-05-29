package com.example.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .claim("roles", List.of("ROLE_USER", "ROLE_ADMIN","ROLE_CUSTOMER"))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // Set expiration (e.g., 10 hours)
                .signWith(SECRET_KEY)
                .compact();
    }
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("roles", List.of("ROLE_USER", "ROLE_ADMIN","ROLE_CUSTOMER"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))  // 30 days
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        return !extractClaims(token).getExpiration().before(new Date());
    }

    public Claims extractClaims(String token) {
//        SecretKey key = Keys.hmacShaKeyFor("your-256-bit-secret".getBytes());

        Jwt<?, Claims> jwt = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token);
        return jwt.getPayload();
    }


    public boolean validateToken(String token, String email) {
        return (email.equals(extractEmail(token)) && isTokenExpired(token));
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractClaims(token);

        List<String> roles = claims.get("roles", List.class);
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
