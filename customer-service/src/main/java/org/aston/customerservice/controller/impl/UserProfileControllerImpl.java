package org.aston.customerservice.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aston.customerservice.controller.UserProfileController;
import org.aston.customerservice.dto.request.ChangePasswordRequestDto;
import org.aston.customerservice.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.aston.customerservice.configuration.ApplicationConstant.HEADER_KEY_CUSTOMER_PHONE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/profile")
public class UserProfileControllerImpl implements UserProfileController {

    private final UserProfileService userProfileService;

    @Override
    @PatchMapping("/change_password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDto dto,
                                       @RequestHeader(HEADER_KEY_CUSTOMER_PHONE) String phoneNumber) {
        userProfileService.changePassword(dto, phoneNumber);
        return ResponseEntity.ok("Пароль успешно изменен");
    }
}
