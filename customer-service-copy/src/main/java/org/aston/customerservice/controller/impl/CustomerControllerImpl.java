package org.aston.customerservice.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.customerservice.controller.CustomerController;
import org.aston.customerservice.dto.response.CustomerResponseDto;
import org.aston.customerservice.dto.request.EmailRequestDto;
import org.aston.customerservice.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.aston.customerservice.configuration.ApplicationConstant.HEADER_KEY_CUSTOMER_PHONE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/settings")
public class CustomerControllerImpl implements CustomerController {

    private final CustomerService customerService;

    @Override
    @GetMapping("/info")
    public ResponseEntity<CustomerResponseDto> findCustomerInfo(
                           @RequestHeader(HEADER_KEY_CUSTOMER_PHONE) String customerPhone) {
        return ResponseEntity.ok(customerService.findByPhoneNumber(customerPhone));
    }

    @Override
    @PatchMapping("/new_email")
    public ResponseEntity<String> updateEmail(@Valid @RequestBody EmailRequestDto emailDto,
                            @RequestHeader(HEADER_KEY_CUSTOMER_PHONE) String customerPhone) {
        customerService.updateEmail(emailDto, customerPhone);
        return ResponseEntity.ok().body("Email успешно изменен");
    }

    @Override
    @PostMapping("/add_email")
    public ResponseEntity<String> addEmail(@Valid @RequestBody EmailRequestDto emailDto,
                            @RequestHeader(HEADER_KEY_CUSTOMER_PHONE) String customerPhone) {
        customerService.addEmail(emailDto, customerPhone);
        return ResponseEntity.status(HttpStatus.CREATED).body("Email успешно добавлен");
    }
}
