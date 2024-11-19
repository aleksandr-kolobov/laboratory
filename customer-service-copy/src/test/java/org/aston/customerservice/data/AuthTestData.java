package org.aston.customerservice.data;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.aston.customerservice.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthTestData {

    private AuthTestData() {
    }

    public static final String WRONG_HEADER= "WRONG_HEADER_NAME";

    public static final String WRONG_TOKEN_PREFIX = "WRONG_PREFIX";

    public static final String REDIS_KEY_PREFIX = "refresh:";

    public static final String CUSTOMER_ID = "7ccbd32e-cdd7-490c-9446-dd716d236fc5";

    public static final String REFRESH_HEADER = "Refresh";

    public static final String PASSWORD = "Password@123";

    public static final String PHONE_NUMBER = "79234251422";

    public static final String EXCEPTED_PHONE = "79234251212";

    public static final String EXCEPTED_EMAIL = "Ivan@mail.ru";

    public static final String TEST_EMAIL = "test@email.ru";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String ACCESS_TOKEN = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxMjM0NTY3ODkiLCJpYXQiOjE3MDc0NjczODQsImV4cCI6MTcwNzQ3MzM4NH0.zEvCZl71HsVaDUdxEv1rnIY6dMnLlt7BRfKoEnF3isxwaz3N3VsOYTV0-zYSLN11";

    public static final String REFRESH_TOKEN = "eyJhbGciOiJIUzM4NCJ9.eyJjdXN0b21lcklkIjoiN2NjYmQzMmUtY2RkNy00OTBjLTk0NDYtZGQ3MTZkMjM2ZmM1Iiwic3ViIjoiNzkyMzQyNTE0MjIiLCJpYXQiOjE3MDk4NDEwNzQsImV4cCI6MTcwOTg0Mjg3NH0.96Iacx2Lbl7D_k0JLbQhvfQWJ5GpSaLZA7Lw7HF7Jn5TJmvE6UMDoL0kpke8lwG6";

    public static final String NEW_REFRESH_TOKEN = "eyJhbGciOiJIUzM4NCJ9.eyJjdXN0b21lcklkIjoiN2NjYmQzMmUtY2RkNy00OTBjLTk0NDYtZGQ3MTZkMjM2ZmM1Iiwic3ViIjoiNzkyMzQyNTE0MjIiLCJpYXQiOjE3MDk4NDE0NDksImV4cCI6MTcwOTg0MzI0OX0.XAQ7J1D_ysnLailXb-BONOIh5GwfHEfrRPTrK3CB0WvROS7qtgteBP2B63ZCUK7-";

    public static final String REFRESH_TOKEN_EXPIRED_OR_REPEATED = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxMjM0NTY3ODkiLCJpYXQiOjE3MDc0NjM0NDEsImV4cCI6MTcwNzQ2MzQ0MX0.nQhHtSerV0cEKgjfiOkOzv_OTCFQ111RBI_AFIlYMnyzytg_8uvBKxnUU-KKikHQ";

    public static final String REFRESH_TOKEN_WRONG_SIGNATURE = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxMjM0NTY3ODkiLCJpYXQiOjE3MDc0MjIyNzMsImV4cCI6MTcwNzQyMjI3M30.xg9lr1quAOXgf3WRlLje3OmeRbhYXwQIiZBUaVv3OJDhQPqhKQu3XSbTrOn74K5sNO";


    public static final UserDetails USER_DETAILS = new CustomUserDetails(PHONE_NUMBER, CUSTOMER_ID);

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
