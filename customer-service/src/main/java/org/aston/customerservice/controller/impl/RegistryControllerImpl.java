package org.aston.customerservice.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aston.customerservice.controller.RegistryController;
import org.aston.customerservice.dto.request.PasswordRecoveryRequestDto;
import org.aston.customerservice.dto.request.UserProfileCreationRequestDto;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.response.CustomerIdResponseDto;
import org.aston.customerservice.dto.response.StatusResponseDto;
import org.aston.customerservice.service.CustomerService;
import org.aston.customerservice.service.StatusService;
import org.aston.customerservice.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/registry")
public class RegistryControllerImpl implements RegistryController {

    private final CustomerService customerService;
    private final StatusService statusService;
    private final UserProfileService userProfileService;

    @Override
    @PostMapping("/check_status")
    public ResponseEntity<StatusResponseDto> getCustomerStatusType(@Valid @RequestBody PhoneNumberRequestDto phoneNumberDto) {
        StatusResponseDto statusResponseDto = statusService.getCustomerStatus(phoneNumberDto);
        return ResponseEntity.ok(statusResponseDto);
    }

    @Override
    @PostMapping("/check_miss_registration")
    public ResponseEntity<CustomerIdResponseDto> checkMissRegistration(@Valid @RequestBody PhoneNumberRequestDto phoneNumberDto) {
        CustomerIdResponseDto customerIdResponseDto = customerService.checkMissRegistration(phoneNumberDto);
        return ResponseEntity.ok(customerIdResponseDto);
    }

    @Override
    @PostMapping("/create_user_profile")
    public ResponseEntity<Void> createPersonalAccount(@Valid @RequestBody UserProfileCreationRequestDto requestDto) {
        userProfileService.createUserProfile(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PostMapping("/recovery_password")
    public ResponseEntity<Void> recoveryPassword(@Valid @RequestBody PasswordRecoveryRequestDto requestDto) {
        userProfileService.recoveryPassword(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
