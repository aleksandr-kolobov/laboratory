package org.aston.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.function.Function;
import static org.aston.apigateway.configuration.ApplicationConstant.*;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Jws<Claims> validateToken(String token) {

        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token);
    }

    public String extractCustomerPhone(Claims claims) {
        return extractClaim(claims, Claims::getSubject);
    }

    public String extractCustomerId(Claims claims) {
        return (String)claims.get(HEADER_KEY_CUSTOMER_ID);
    }

    public <T> T extractClaim(Claims claims, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(claims);
    }
}