package org.aston.cardservice.exception.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.aston.cardservice.exception.CardServiceGenericException;
import org.aston.cardservice.exception.MinioSecurityException;
import org.aston.cardservice.exception.ProcessingImageFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class AppExceptionHandler {

    @ExceptionHandler({ProcessingImageFailureException.class, MinioSecurityException.class})
    public ResponseEntity<String> handleMinioException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleGenericMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Некорректные данные: {}", exception.getMessage(), exception);
        exception.getStatusCode();
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleMethodConstraintViolationException(ConstraintViolationException exception) {
        String errorMessage = exception.getConstraintViolations().iterator().next().getMessage();
        log.error(errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }

    @ExceptionHandler({CardServiceGenericException.class})
    public ResponseEntity<String> handleGenericCardProductException(CardServiceGenericException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status((exception.getStatus() != null) ? exception.getStatus() : HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
