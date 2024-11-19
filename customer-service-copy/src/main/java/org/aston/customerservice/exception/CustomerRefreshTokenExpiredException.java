package org.aston.customerservice.exception;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;

public class CustomerRefreshTokenExpiredException extends ExpiredJwtException {

    public CustomerRefreshTokenExpiredException(Header header, Claims claims) {
        super(header, claims, "Срок хранения токена обновления истек");
    }
}
