package org.aston.customerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aston.customerservice.dto.request.PasswordRecoveryRequestDto;
import org.aston.customerservice.dto.request.UserProfileCreationRequestDto;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.response.CustomerIdResponseDto;
import org.aston.customerservice.dto.response.StatusResponseDto;
import org.springframework.http.ResponseEntity;

@Tag(name = "Контроллер для регистрации", description = "Customer API version v1")
public interface RegistryController {

    @Operation(
            summary = "Проверить статус клиента",
            description = "Проверить тип статуса клиента",
            tags = {"profile", "customer"}
    )
    ResponseEntity<StatusResponseDto> getCustomerStatusType(PhoneNumberRequestDto phoneNumberDto);

    @Operation(
            summary = "Проверить отсутствие личного кабинета",
            description = "Проверить отсутствие личного кабинета у клиента перед регистрацией",
            tags = {"profile", "customer"}
    )
    ResponseEntity<CustomerIdResponseDto> checkMissRegistration(PhoneNumberRequestDto phoneNumberDto);

    @Operation(
            summary = "Создать учетную запись",
            description = "Зарегистрироваться (создать учетную запись)",
            tags = {"profile"}
    )
    ResponseEntity<Void> createPersonalAccount(UserProfileCreationRequestDto requestDto);

    @Operation(
            summary = "Восстановить пароль",
            description = "Восстановить пароль для учетной записи",
            tags = {"profile"}
    )
    ResponseEntity<Void> recoveryPassword(PasswordRecoveryRequestDto requestDto);

}
