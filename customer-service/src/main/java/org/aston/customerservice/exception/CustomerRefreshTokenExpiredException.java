package org.aston.customerservice.exception;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;

import static org.aston.customerservice.configuration.ApplicationConstant.MESSAGE_EXPIRED_REFRESH_JWT;

public class CustomerRefreshTokenExpiredException extends ExpiredJwtException {

    public CustomerRefreshTokenExpiredException(Header header, Claims claims) {
        super(header, claims, MESSAGE_EXPIRED_REFRESH_JWT);
    }
}
