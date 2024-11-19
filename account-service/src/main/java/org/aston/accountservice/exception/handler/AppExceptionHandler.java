package org.aston.accountservice.exception.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.aston.accountservice.exception.AccountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({AccountException.class})
    public ResponseEntity<String> handleGenericAccountNotFoundException(AccountException exception) {
        log.error("Некорректные данные: {}", exception.getMessage());
        return ResponseEntity
                .status((exception.getStatus() != null) ? exception.getStatus() : HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleGenericConstraintViolationException(ConstraintViolationException exception) {
        StringBuilder errorMessage = new StringBuilder();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            errorMessage.append(violation.getMessage()).append(". ");
        }
        log.error("Некорректные данные: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage.toString());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleGenericMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Некорректные данные: {}", exception.getMessage());
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
    }

}
