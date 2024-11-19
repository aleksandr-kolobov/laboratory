package org.aston.customerservice.exception;

import lombok.Getter;

@Getter
public class IllegalArgumentProcessingJwtException extends RuntimeException {

    public IllegalArgumentProcessingJwtException(String message) {
        super(message);
    }
}
