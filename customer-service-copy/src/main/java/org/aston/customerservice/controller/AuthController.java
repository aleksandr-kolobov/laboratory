package org.aston.customerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.aston.customerservice.dto.request.AuthRequestDto;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.response.AuthResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag(name = "Контроллер для авторизации", description = "Customer API version v1")
public interface AuthController {

    @Operation(
            summary = "Авторизоваться",
            description = "Авторизоваться и получить токены",
            tags = {"token"}
    )
    ResponseEntity<AuthResponseDto> authenticateAndGetToken(AuthRequestDto authRequestDto);

    @Operation(
            summary = "Обновить токен",
            description = "Обновить токен доступа по токену обновления",
            tags = {"token"}
    )
    ResponseEntity<AuthResponseDto> getNewAccessToken(HttpServletRequest request);

    @Operation(
            summary = "Проверить наличие личного кабинета",
            description = "Проверить наличие личного кабинета у клиента перед авторизацией",
            tags = {"profile", "customer"}
    )
    HttpStatus checkRegistration(PhoneNumberRequestDto phoneNumberDto);

}
