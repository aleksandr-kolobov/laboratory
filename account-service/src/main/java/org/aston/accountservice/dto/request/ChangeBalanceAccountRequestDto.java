package org.aston.accountservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static org.aston.accountservice.configuration.ApplicationConstant.REGEXP_ACCOUNT_NUMBER;
import static org.aston.accountservice.configuration.ApplicationConstant.REGEXP_ACTION;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangeBalanceAccountRequestDto {

    @Pattern(regexp = REGEXP_ACCOUNT_NUMBER, message = "Некоректные данные номера счета")
    @NotEmpty(message = "Номер аккаунта не указан")
    private String accountNumber;

    @NotNull(message = "Сумма операции не указана")
    @Positive(message = "Сумма операции должна быть больше 0")
    private BigDecimal amount;

    @NotBlank(message = "Не указана операция со счетом")
    @Pattern(message = "Нет такой операции со счетом", regexp = REGEXP_ACTION)
    private String action;
}
