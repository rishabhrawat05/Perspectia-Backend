package com.perspectia.perspectiabackend.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    public JWTAuthenticationFilter(JwtHelper jwtHelper, UserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Log all incoming requests for debugging
        System.out.println("=== JWT Filter ===");
        System.out.println("Path: " + path);
        System.out.println("Method: " + method);

        // Skip JWT authentication for public auth endpoints except /me and /validate
        if (path.startsWith("/api/perspectia/auth/") &&
                !path.equals("/api/perspectia/auth/me") &&
                !path.equals("/api/perspectia/auth/validate")) {
            System.out.println("Skipping JWT auth for public endpoint: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;

        // Check Authorization header first (for API clients)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("Token found in Authorization header");
        }

        // Fall back to cookies (for browser clients)
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    System.out.println("Token found in cookie");
                    break;
                }
            }
        }

        if (token == null) {
            System.out.println("No token found, continuing without auth");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            System.out.println("Validating JWT token...");
            String email = jwtHelper.getEmailFromToken(token);

            if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null &&
                    !jwtHelper.isTokenExpired(token)) {

                System.out.println("Loading user details for: " + email);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication successful for: " + email);
            }

        } catch (Exception e) {
            System.err.println("JWT validation failed: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}