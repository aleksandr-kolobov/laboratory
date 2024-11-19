package org.aston.customerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aston.customerservice.dto.request.EmailRequestDto;
import org.aston.customerservice.dto.response.CustomerResponseDto;
import org.springframework.http.ResponseEntity;

@Tag(name = "Контроллер для работы с клиентами", description = "Customer API version v1")
public interface CustomerController {

    @Operation(
            summary = "Получить информацию",
            description = "Получить информацию о клиенте",
            tags = {"customer"}
    )
    ResponseEntity<CustomerResponseDto> findCustomerInfo(String customerPhone);

    @Operation(
            summary = "Внести электронную почту",
            description = "Внести email в данные клиента",
            tags = {"customer"}
    )
    ResponseEntity<String> addEmail(EmailRequestDto emailDto, String customerPhone);

    @Operation(
            summary = "Изменить электронную почту",
            description = "Изменить email в данных клиента",
            tags = {"customer"}
    )
    ResponseEntity<String> updateEmail(EmailRequestDto emailDto, String customerPhone);

}
