package org.aston.customerservice.exception;

import lombok.Getter;

@Getter
public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String message) {
        super(message);
    }
}
