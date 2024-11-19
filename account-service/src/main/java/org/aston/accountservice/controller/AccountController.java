package org.aston.accountservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.aston.accountservice.dto.request.ChangeBalanceAccountRequestDto;
import org.aston.accountservice.dto.request.MasterAccountRequestDto;
import org.aston.accountservice.dto.response.AccountResponseDto;
import org.aston.accountservice.dto.response.GetAccountsListResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.aston.accountservice.configuration.ApplicationConstant.HEADER_KEY_CUSTOMER_ID;
import static org.aston.accountservice.configuration.ApplicationConstant.REGEXP_ACCOUNT_NUMBER;

@Tag(name = "Контроллер для работы со счетами клиента", description = "Account API version v1")
public interface AccountController {

    @Operation(
            summary = "Измение статуса MasterAccount статуса",
            description = "Сделать аккаунт основным",
            tags = {"account"})
    ResponseEntity<String> changeMasterAccountStatus(@Valid @RequestBody MasterAccountRequestDto masterAccountRequestDto,
                                                     @RequestHeader(HEADER_KEY_CUSTOMER_ID) String customerId);

    @Operation(
            summary = "Измение баланса счета",
            description = "Изменить баланс на счете клиента",
            tags = {"account"})
    ResponseEntity<String> changeBalance(@Valid @RequestBody ChangeBalanceAccountRequestDto
                                                 changeBalanceDto, @RequestHeader(HEADER_KEY_CUSTOMER_ID) String customerId);

    @Operation(
            summary = "Получение информации о счете",
            description = "Просмотр информации о счете клиента",
            tags = {"account"})
    ResponseEntity<AccountResponseDto> findAccountInfo(@RequestHeader(HEADER_KEY_CUSTOMER_ID) String
                                                               customerId, @Pattern(regexp = REGEXP_ACCOUNT_NUMBER, message = "Неккоректные данные") @NotBlank(message = "Номер аккаунта не может быть пустым") @RequestParam(name = "account_number") String
                                                               accountNumber);

    @Operation(
            summary = "Получение списка счетов",
            description = "Просмотр списка счетов клиента",
            tags = {"account"})
    ResponseEntity<List<GetAccountsListResponseDto>> getAccountList(@RequestHeader(HEADER_KEY_CUSTOMER_ID) String customerId);
}
