package org.aston.customerservice.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import static org.aston.customerservice.configuration.ApplicationConstant.HEADER_KEY_CUSTOMER_ID;

@Slf4j
@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.jwt-expiration}")
    private Duration jwtExpiration;
    @Value("${jwt.jwt-refresh-expiration}")
    private Duration jwtRefreshExpiration;

    public String generateToken(String userName, String customerId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(HEADER_KEY_CUSTOMER_ID, customerId);
        return createAccessToken(claims, userName);
    }

    public String extractCustomerId(Claims claims) {
        return (String) claims.get(HEADER_KEY_CUSTOMER_ID);
    }

    public String generateRefreshToken(String userName, String customerId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(HEADER_KEY_CUSTOMER_ID, customerId);
        return createRefreshToken(claims, userName);
    }

    private String createAccessToken(Map<String, Object> claims, String phoneNumber) {
        return Jwts.builder()
                .claims(claims)
                .subject(phoneNumber)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration.toMillis()))
                .signWith(getSignKey())
                .compact();
    }

    private String createRefreshToken(Map<String, Object> claims, String phoneNumber) {
        return Jwts.builder()
                .claims(claims)
                .subject(phoneNumber)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration.toMillis()))
                .signWith(getSignKey())
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token);
    }

    public String extractPhoneNumber(Claims claims) {
        return extractClaim(claims, Claims::getSubject);
    }

    private <T> T extractClaim(Claims claims, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(claims);
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
