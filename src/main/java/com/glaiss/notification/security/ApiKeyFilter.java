package com.glaiss.notification.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("#{'${spring.chave-de-acesso}'.split(',')}")
    private List<String> validApiKeys;

    private static final List<String> publicPaths = List.of(
            "/notification/actuator/info",
            "/notification/actuator/health"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader("X-CHAVE-DE-ACESSO");

        if (Objects.isNull(apiKey) || apiKey.isBlank() || !validApiKeys.contains(apiKey)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Chave de acesso inválida");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return publicPaths.contains(path);
    }
}
