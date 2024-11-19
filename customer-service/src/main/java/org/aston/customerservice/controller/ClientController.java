package org.aston.customerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aston.customerservice.dto.response.CustomerFullNameResponseDto;
import org.aston.customerservice.dto.response.StatusTypeResponseDto;
import org.springframework.http.ResponseEntity;

@Tag(name = "Контроллер для передачи данных в другой сервис", description = "Customer API version v1")
public interface ClientController {

    @Operation(
            summary = "Получить информацию ФИО",
            description = "Получить информацию ФИО о клиенте",
            tags = {"customer"}
    )
    ResponseEntity<CustomerFullNameResponseDto> getCustomerFullName(String customerId);

    @Operation(
            summary = "Проверить статус клиента",
            description = "Проверить тип статуса клиента",
            tags = {"profile", "customer"}
    )
    StatusTypeResponseDto getCustomerStatusTypeById(String customerId);
}
