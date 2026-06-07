package com.cortex.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

public class JwtUtil {
    private static final SecretKey KEY;

    static {
        var secret = System.getenv("JWT_SECRET");
        if (secret == null || secret.isEmpty()) {
            secret = "cortex-dev-jwt-secret-key-change-in-production-12345";
            System.err.println("WARNING: JWT_SECRET environment variable not set. Using insecure default. Set JWT_SECRET in production.");
        }
        if (secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET must be at least 32 characters long");
        }
        KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private static final long EXPIRATION = 86400000L;

    public static String generateToken(Long userId, String email, Set<String> roles) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    public static Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static Long getUserId(String token) {
        return Long.parseLong(validateToken(token).getSubject());
    }

    public static Set<String> getRoles(String token) {
        @SuppressWarnings("unchecked")
        var roles = validateToken(token).get("roles", java.util.List.class);
        return Set.copyOf(roles);
    }
}
