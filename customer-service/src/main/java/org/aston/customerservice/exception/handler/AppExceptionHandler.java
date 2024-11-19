package org.aston.customerservice.exception.handler;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.aston.customerservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.ConnectException;
import java.util.Objects;

import static org.aston.customerservice.configuration.ApplicationConstant.MESSAGE_INCORRECT_DATA;
import static org.aston.customerservice.configuration.ApplicationConstant.UNSUPPORTED_CONTENT_TYPE;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({AuthorizationException.class,
            IllegalArgumentProcessingJwtException.class,
            CustomerRefreshTokenExpiredException.class})
    public ResponseEntity<String> handleGenericAuthorizationException(RuntimeException exception) {
        log.error("Ошибка авторизации: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exception.getMessage());
    }

    @ExceptionHandler({JwtException.class})
    public ResponseEntity<String> handleJwtException(JwtException exception) {
        log.error(MESSAGE_INCORRECT_DATA + ": {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exception.getMessage());
    }

    @ExceptionHandler({CustomerException.class})
    public ResponseEntity<String> handleGenericCustomerException(CustomerException exception) {
        log.error(MESSAGE_INCORRECT_DATA + ": {}", exception.getMessage());
        return ResponseEntity
                .status((exception.getStatus() != null) ? exception.getStatus() : HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler({UserProfileException.class})
    public ResponseEntity<String> handleGenericUserProfileException(UserProfileException exception) {
        log.error(MESSAGE_INCORRECT_DATA + ": {}", exception.getMessage());
        return ResponseEntity
                .status((exception.getStatus() != null) ? exception.getStatus() : HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler({VerificationException.class})
    public ResponseEntity<String> handleGenericVerificationException(VerificationException exception) {
        log.error(MESSAGE_INCORRECT_DATA + ": {}", exception.getMessage());
        return ResponseEntity
                .status((exception.getStatus() != null) ? exception.getStatus() : HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler({ConnectException.class})
    public ResponseEntity<String> handleGenericConnectException(ConnectException exception) {
        log.error("Невозможно установить соединение: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Ошибка подключения к ресурсу");
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleGenericHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("Ошибка при чтении HTTP сообщения: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Некорректный JSON");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleGenericMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(MESSAGE_INCORRECT_DATA + ": {}", exception.getMessage());
        exception.getStatusCode();
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<String> handleGenericHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        log.error(UNSUPPORTED_CONTENT_TYPE + ": {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(UNSUPPORTED_CONTENT_TYPE);
    }
}
