package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Service xử lý JWT token:
 *  - Validate token (chữ ký, hết hạn)
 *  - Trích xuất userId và username từ claims
 */
@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    @Value("${jwt.secret}")
    private String secretKey;

    // ------------------------------------------------------------------ //
    //  Private helpers
    // ------------------------------------------------------------------ //

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ------------------------------------------------------------------ //
    //  Public API
    // ------------------------------------------------------------------ //

    /**
     * Validate token: kiểm tra chữ ký và thời hạn.
     *
     * @param token JWT token (không có tiền tố "Bearer ")
     * @return {@code true} nếu token hợp lệ
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Trích xuất subject (thường là username / email) từ token.
     *
     * @param token JWT token hợp lệ
     * @return subject được lưu trong claim "sub"
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Trích xuất userId từ claim "userId" trong token.
     * Trả về {@code null} nếu claim không tồn tại.
     *
     * @param token JWT token hợp lệ
     * @return userId hoặc null
     */
    public Integer extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object userId = claims.get("userId");
        if (userId == null) {
            return null;
        }
        // jjwt deserialize số nguyên thành Integer hoặc Long
        if (userId instanceof Integer) {
            return (Integer) userId;
        }
        if (userId instanceof Long) {
            return ((Long) userId).intValue();
        }
        return Integer.valueOf(userId.toString());
    }

    /**
     * Lấy raw token từ Authorization header.
     * Header phải có dạng: "Bearer <token>"
     *
     * @param authHeader giá trị của header Authorization
     * @return raw token hoặc {@code null} nếu header không hợp lệ
     */
    public String resolveToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}

