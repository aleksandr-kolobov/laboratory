package org.aston.customerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aston.customerservice.dto.request.ChangePasswordRequestDto;
import org.springframework.http.ResponseEntity;

@Tag(name = "Контроллер для работы с личным кабинетом", description = "Customer API version v1")
public interface UserProfileController {

    @Operation(
            summary = "Изменить пароль",
            description = "Изменить пароль авторизованного клиента",
            tags = {"profile"}
    )
    ResponseEntity<String> changePassword(ChangePasswordRequestDto dto, String customerPhone);
}
