package org.aston.customerservice.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aston.customerservice.controller.VerificationController;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.request.VerificationRequestDto;
import org.aston.customerservice.service.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/auth/verification")
public class VerificationControllerImpl implements VerificationController {

    private final VerificationService verificationService;

    @Override
    @PostMapping("/generate_code")
    public ResponseEntity<String> generateCode(@Valid @RequestBody PhoneNumberRequestDto phoneNumberDto) {
        verificationService.sendCode(phoneNumberDto.getPhoneNumber());
        return ResponseEntity.ok("Код верификации отправлен в СМС");
    }

    @Override
    @PostMapping("/check_code")
    public ResponseEntity<String> checkCode(@Valid @RequestBody VerificationRequestDto verificationDto) {
        verificationService.checkCode(verificationDto.getPhoneNumber(), verificationDto.getCode());
        return ResponseEntity.ok("Код верификации подтвержден");
    }
}
