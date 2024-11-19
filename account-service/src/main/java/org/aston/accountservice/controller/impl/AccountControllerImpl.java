package org.aston.accountservice.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.aston.accountservice.controller.AccountController;
import org.aston.accountservice.dto.request.ChangeBalanceAccountRequestDto;
import org.aston.accountservice.dto.request.MasterAccountRequestDto;
import org.aston.accountservice.dto.response.AccountResponseDto;
import org.aston.accountservice.dto.response.GetAccountsListResponseDto;
import org.aston.accountservice.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.aston.accountservice.configuration.ApplicationConstant.HEADER_KEY_CUSTOMER_ID;
import static org.aston.accountservice.configuration.ApplicationConstant.REGEXP_ACCOUNT_NUMBER;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountControllerImpl implements AccountController {

    private final AccountService service;

    @Override
    @PatchMapping("/accounts/update/master_account")
    public ResponseEntity<String> changeMasterAccountStatus(@Valid @RequestBody MasterAccountRequestDto masterAccountRequestDto,
                                                            @RequestHeader(HEADER_KEY_CUSTOMER_ID) String customerId) {
        service.changeMasterAccountStatus(masterAccountRequestDto, customerId);
        return ResponseEntity.ok("Статус аккаунта изменён");
    }

    @Override
    @PatchMapping("/change_balance")
    public ResponseEntity<String> changeBalance(@Valid @RequestBody ChangeBalanceAccountRequestDto
                                                        changeBalanceDto, @RequestHeader(HEADER_KEY_CUSTOMER_ID) String customerId) {
        service.changeBalance(changeBalanceDto, customerId);
        return ResponseEntity.ok("Баланс успешно изменен");
    }

    @Override
    @GetMapping("/information/")
    public ResponseEntity<AccountResponseDto> findAccountInfo(@RequestHeader(HEADER_KEY_CUSTOMER_ID) String customerId,
                              @Pattern(regexp = REGEXP_ACCOUNT_NUMBER, message = "Неккоректные данные")
                              @NotBlank(message = "Номер аккаунта не может быть пустым")
                              @RequestParam(name = "account_number") String accountNumber) {
        return ResponseEntity.ok(service.getAccountInfo(accountNumber, customerId));
    }

    @Override
    @GetMapping("/list_account_number")
    public ResponseEntity<List<GetAccountsListResponseDto>> getAccountList
            (@RequestHeader(HEADER_KEY_CUSTOMER_ID) String customerId) {
        return ResponseEntity.ok().body(service.findAccountList(customerId));
    }
}
