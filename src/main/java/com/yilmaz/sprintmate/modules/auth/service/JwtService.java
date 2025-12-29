package com.yilmaz.sprintmate.modules.auth.service;

import com.yilmaz.sprintmate.modules.auth.config.JwtProperties;
import com.yilmaz.sprintmate.modules.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * JWT Token Service
 * 
 * Token generation, validation and claim extraction operations
 * 
 * Best Practices:
 * - Constructor injection (Lombok @RequiredArgsConstructor)
 * - Type-safe config (JwtProperties)
 * - Proper exception handling
 * - Logging
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    /**
     * Generate JWT token for user
     * 
     * @param user User entity
     * @return JWT token string
     */
    public String generateToken(User user) {
        Map<String, Object> claims = buildClaims(user);

        String token = Jwts.builder()
                .claims(claims)
                .subject(user.getId().toString())
                .issuer(jwtProperties.getIssuer())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(getSigningKey())
                .compact();

        log.debug("Generated JWT token for user: {}", user.getUsername());
        return token;
    }

    /**
     * Build token claims
     */
    private Map<String, Object> buildClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().toString());
        claims.put("username", user.getUsername());
        claims.put("githubId", user.getGithubId());

        if (user.getRole() != null) {
            claims.put("role", user.getRole());
        }

        return claims;
    }

    /**
     * Extract User ID from token
     */
    public UUID extractUserId(String token) {
        String subject = extractAllClaims(token).getSubject();
        return UUID.fromString(subject);
    }

    /**
     * Extract username from token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    }

    /**
     * Extract role from token
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    /**
     * Validate token
     * 
     * @return true: valid, false: invalid or expired
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            boolean notExpired = !claims.getExpiration().before(new Date());

            if (!notExpired) {
                log.debug("Token has expired");
            }

            return notExpired;
        } catch (ExpiredJwtException e) {
            log.debug("Token expired: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Malformed token: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.warn("Invalid token signature: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extract all claims from token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * HMAC-SHA256 signing key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Return token expiration time in seconds
     */
    public Long getExpirationInSeconds() {
        return jwtProperties.getExpiration() / 1000;
    }
}
