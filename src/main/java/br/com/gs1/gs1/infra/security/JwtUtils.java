package br.com.gs1.gs1.infra.security;

import jakarta.servlet.http.HttpServletRequest;

public class JwtUtils {
    public static String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null) {
            return null;
        }
        return header.substring(7);
    }
}
