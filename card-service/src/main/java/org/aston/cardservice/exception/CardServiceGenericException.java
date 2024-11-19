package org.aston.cardservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class CardServiceGenericException extends RuntimeException {

    private final HttpStatus status;

    public CardServiceGenericException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
