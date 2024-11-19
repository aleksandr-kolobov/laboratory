package org.aston.customerservice.controller.impl;

import lombok.RequiredArgsConstructor;
import org.aston.customerservice.controller.ClientController;
import org.aston.customerservice.dto.response.CustomerFullNameResponseDto;
import org.aston.customerservice.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.aston.customerservice.dto.response.StatusTypeResponseDto;
import org.aston.customerservice.service.StatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.aston.customerservice.configuration.ApplicationConstant.HEADER_KEY_CUSTOMER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class ClientControllerImpl implements ClientController {

    private final CustomerService customerService;

    private final StatusService statusService;

    @Override
    @GetMapping("/full_name")
    public ResponseEntity<CustomerFullNameResponseDto> getCustomerFullName(
                    @RequestHeader(value = HEADER_KEY_CUSTOMER_ID) String customerId) {
        return ResponseEntity.ok(customerService.getFullName(customerId));
    }

    @Override
    @GetMapping("/status_type")
    public StatusTypeResponseDto getCustomerStatusTypeById(@RequestHeader(HEADER_KEY_CUSTOMER_ID) String customerId) {
        return statusService.getStatusType(customerId);
    }
}
