package org.aston.customerservice.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aston.customerservice.controller.AuthController;
import org.aston.customerservice.dto.request.AuthRequestDto;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.response.AuthResponseDto;
import org.aston.customerservice.service.AuthService;
import org.aston.customerservice.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/auth")
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    private final CustomerService customerService;

    @Override
    @PostMapping("/refresh_token")
    public ResponseEntity<AuthResponseDto> getNewAccessToken(HttpServletRequest request) {
        return ResponseEntity.ok(authService.refreshJwt(request));
    }

    @Override
    @PostMapping("/generate_token")
    public ResponseEntity<AuthResponseDto> authenticateAndGetToken(@Valid @RequestBody AuthRequestDto authRequestDto) {
        return ResponseEntity.ok(authService.authorizeUser(authRequestDto));
    }

    @Override
    @PostMapping("/check_registration")
    public HttpStatus checkRegistration(@Valid @RequestBody PhoneNumberRequestDto phoneNumberDto) {
        customerService.checkRegistration(phoneNumberDto);
        return HttpStatus.OK;
    }

}
