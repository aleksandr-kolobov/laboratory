package org.aston.accountservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccountException extends RuntimeException {

    private HttpStatus status;

    public AccountException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public AccountException(String message) {
        super(message);
    }
}
