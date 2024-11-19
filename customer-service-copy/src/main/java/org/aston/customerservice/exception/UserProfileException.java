package org.aston.customerservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserProfileException extends RuntimeException {

    private HttpStatus status;

    public UserProfileException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public UserProfileException(String message) {
        super(message);
    }
}
