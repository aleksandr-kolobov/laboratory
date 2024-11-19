package org.aston.apigateway.data;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthTestData {
    private AuthTestData() {
    }
    public static final String PHONE_NUMBER = "79234251422";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_EXPIRED_OR_REPEATED = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxMjM0NTY3ODkiLCJpYXQiOjE3MDc0NjM0NDEsImV4cCI6MTcwNzQ2MzQ0MX0.nQhHtSerV0cEKgjfiOkOzv_OTCFQ111RBI_AFIlYMnyzytg_8uvBKxnUU-KKikHQ";
    public static final String TOKEN_WRONG_SIGNATURE = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxMjM0NTY3ODkiLCJpYXQiOjE3MDc0MjIyNzMsImV4cCI6MTcwNzQyMjI3M30.xg9lr1quAOXgf3WRlLje3OmeRbhYXwQIiZBUaVv3OJDhQPqhKQu3XSbTrOn74K5sNO";

    public static String createToken(String phoneNumber, String secret, Long jwtExpiration) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(phoneNumber)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .compact();
    }
}
