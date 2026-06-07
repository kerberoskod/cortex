package com.cortex.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

public class JwtUtil {
    private static final String SECRET = "cortex-secret-key-must-be-at-least-256-bits-long-for-hs256";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
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
