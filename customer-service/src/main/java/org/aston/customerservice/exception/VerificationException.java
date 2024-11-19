package org.aston.customerservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class VerificationException extends RuntimeException {

    private HttpStatus status;

    public VerificationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public VerificationException(String message) {
        super(message);
    }
}