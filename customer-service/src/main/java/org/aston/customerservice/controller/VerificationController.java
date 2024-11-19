package org.aston.customerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.request.VerificationRequestDto;
import org.springframework.http.ResponseEntity;

@Tag(name = "Контроллер для кода верификации", description = "Customer API version v1")
public interface VerificationController {

    @Operation(
            summary = "Сгенерировать код верификации",
            description = "Сгенерировать код верификации и отправить клиенту",
            tags = {"profile"}
    )
    ResponseEntity<String> generateCode(PhoneNumberRequestDto phoneNumberDto);

    @Operation(
            summary = "Проверить код верификации",
            description = "Проверить код верификации, полученный от клиента",
            tags = {"profile"}
    )
    ResponseEntity<String> checkCode(VerificationRequestDto verificationDto);
}
