package org.aston.customerservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthorizationException extends RuntimeException {

    private final HttpStatus status;

    public AuthorizationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
