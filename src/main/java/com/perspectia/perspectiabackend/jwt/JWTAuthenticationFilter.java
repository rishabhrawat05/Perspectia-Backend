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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String path = request.getServletPath();

        if (path.equals("/api/perspectia/auth/login") ||
                path.equals("/api/perspectia/auth/signup") ||
                path.equals("/api/perspectia/auth/verify/email") ||
                path.equals("/api/perspectia/auth/resend/otp") ||
                path.equals("/api/perspectia/auth/google-login") ||
                path.equals("/api/perspectia/auth/github-login") ||
                path.equals("/api/perspectia/auth/token/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = null;
        String token = null;

        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            email = jwtHelper.getEmailFromToken(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (!(jwtHelper.isTokenExpired(token))) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                } else {
                    System.out.println("Token is expired or user details not found.");
                }
            }

        }
        filterChain.doFilter(request, response);
    }
}
