package com.rider.it_request_service.security;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Dotenv dotenv = Dotenv.load();
    private final String secretKey = dotenv.get("JWT_SECRET");
    private final long expirationTime = Long.parseLong(dotenv.get("JWT_EXPIRATION"));
    private final Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

    public String generateToken(
            String username, Integer userId, String role, Map<String, Object> claims) {
        claims.put("userId", userId);
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact(); // คืนค่า String
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return validateToken(token).getSubject(); // เเกะข้อมูลใน โทเค็น
    }

    public boolean isTokenExpired(String token) {
        return validateToken(token).getExpiration().before(new Date());
    }
}
