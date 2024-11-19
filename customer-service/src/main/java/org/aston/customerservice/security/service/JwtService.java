package org.aston.customerservice.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.aston.customerservice.configuration.ApplicationConstant.HEADER_KEY_CUSTOMER_ID;
import static org.aston.customerservice.configuration.ApplicationConstant.HEADER_KEY_CUSTOMER_STATUS;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;

    public String generateToken(String phoneNumber, String customerId, String customerStatus, Duration expiration) {
        return buildToken(createClaims(customerId, customerStatus), phoneNumber, expiration);
    }

    private String buildToken(Map<String, Object> claims, String phoneNumber, Duration expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(phoneNumber)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration.toMillis()))
                .signWith(getSignKey())
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token);
    }

    private Map<String, Object> createClaims(String customerId, String customerStatus) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(HEADER_KEY_CUSTOMER_ID, customerId);
        claims.put(HEADER_KEY_CUSTOMER_STATUS, customerStatus);
        return claims;
    }

    public String extractPhoneNumber(Claims claims) {
        return extractClaim(claims, Claims::getSubject);
    }

    private <T> T extractClaim(Claims claims, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(claims);
    }

    public String extractClaim(Claims claims, String claimName) {
        return claims.get(claimName, String.class);
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
