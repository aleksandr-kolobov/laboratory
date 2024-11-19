package org.aston.customerservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomerException extends RuntimeException {

    private HttpStatus status;

    public CustomerException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public CustomerException(String message) {
        super(message);
    }
}
