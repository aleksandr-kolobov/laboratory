package org.aston.customerservice.exception.handler;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.aston.customerservice.exception.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import java.net.ConnectException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.aston.customerservice.configuration.ApplicationConstant.MESSAGE_CUSTOMER_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
public class AppExceptionHandlerTest {
    @InjectMocks
    private AppExceptionHandler appExceptionHandler;

    @Test
    @DisplayName("Тест на JwtException без HttpStatus")
    void testCatchJwtExceptionWithoutHttpStatus() {
        ResponseEntity<String> responseEntity =
                appExceptionHandler.handleJwtException(
                        new JwtException("JwtException"));

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(),"JwtException");
    }

    @Test
    @DisplayName("Тест на CustomerException без HttpStatus")
    void testCatchCustomerExceptionWithoutHttpStatus() {
        ResponseEntity<String> responseEntity =
                appExceptionHandler.handleGenericCustomerException(
                        new CustomerException(MESSAGE_CUSTOMER_NOT_FOUND));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(),MESSAGE_CUSTOMER_NOT_FOUND);
    }

    @Test
    @DisplayName("Тест на CustomerException с HttpStatus")
    void testCatchCustomerExceptionWithHttpStatus() {
        ResponseEntity<String> responseEntity =
                appExceptionHandler.handleGenericCustomerException(
                        new CustomerException(HttpStatus.NOT_FOUND, MESSAGE_CUSTOMER_NOT_FOUND));

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(),MESSAGE_CUSTOMER_NOT_FOUND);
    }

    @Test
    @DisplayName("Тест на ProfileException без HttpStatus")
    void testCatchUserProfileExceptionWithoutHttpStatus() {
        ResponseEntity<String> responseEntity =
                appExceptionHandler.handleGenericUserProfileException(
                        new UserProfileException("Профиль пользователя уже существует!"));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(),"Профиль пользователя уже существует!");
    }

    @Test
    @DisplayName("Тест на ProfileException с HttpStatus")
    void testCatchUserProfileExceptionWithHttpStatus() {
        ResponseEntity<String> responseEntity =
                appExceptionHandler.handleGenericUserProfileException(
                        new UserProfileException(HttpStatus.NOT_FOUND, "Профиль пользователя не найден!"));

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(),"Профиль пользователя не найден!");
    }

    @Test
    @DisplayName("Тест на VerificationException без HttpStatus")
    void testCatchVerificationExceptionWithoutHttpStatus() {
        ResponseEntity<String> responseEntity =
                appExceptionHandler.handleGenericVerificationException(
                        new VerificationException("Неверный код верификации"));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(),"Неверный код верификации");
    }

    @Test
    @DisplayName("Тест на VerificationException с HttpStatus")
    void testCatchVerificationExceptionWithHttpStatus() {
        ResponseEntity<String> responseEntity =
                appExceptionHandler.handleGenericVerificationException(
                        new VerificationException(HttpStatus.NOT_FOUND, "Не найден код верификации"));

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(),"Не найден код верификации");
    }

    @Test
    @DisplayName("Тест на ConnectException без HttpStatus")
    void testCatchConnectExceptionWithoutHttpStatus() {
        ResponseEntity<String> responseEntity =
                appExceptionHandler.handleGenericConnectException(
                        new ConnectException("Ошибка подключения к ресурсу"));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(),"Ошибка подключения к ресурсу");
    }

    @Test
    @DisplayName("Тест на HttpMessageNotReadableException без HttpStatus")
    void testCatchHttpMessageNotReadableExceptionWithoutHttpStatus() {
        ResponseEntity<String> responseEntity =
                appExceptionHandler.handleGenericHttpMessageNotReadableException(
                        new HttpMessageNotReadableException("Некорректный JSON"));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(),"Некорректный JSON");
    }

    @Test
    @DisplayName("Тест на HttpMediaTypeNotSupportedException без HttpStatus")
    void testCatchHttpMediaTypeNotSupportedExceptionWithoutHttpStatus() {
        ResponseEntity<String> responseEntity =
                appExceptionHandler.handleGenericHttpMediaTypeNotSupportedException(
                        new HttpMediaTypeNotSupportedException("Неподдерживаемый тип контента!"));

        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(),"Неподдерживаемый тип контента!");
    }
}
