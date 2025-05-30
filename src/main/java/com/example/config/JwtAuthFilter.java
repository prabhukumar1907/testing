package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.dto.ApiResponse;
import com.example.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
@WebFilter("/api/*")
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/auth/login")||requestURI.startsWith("/api/auth/verifyOtp") || requestURI.startsWith("/api/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromHeader(request);

        if (token != null) {
            String username = jwtService.extractEmail(token);

            if (jwtService.validateToken(token, username)) {
                var authorities = jwtService.extractAuthorities(token);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // Invalid or expired token
                ApiResponse apiResponse = new ApiResponse(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
                return;
            }
        } else {
            ApiResponse apiResponse = new ApiResponse(HttpServletResponse.SC_UNAUTHORIZED, "Missing token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
            return;
        }

        filterChain.doFilter(request, response);
    }


    private String getTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }


    @Override
    public void destroy() {

    }
}
