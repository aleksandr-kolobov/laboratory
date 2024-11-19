package org.aston.accountservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static org.aston.accountservice.configuration.ApplicationConstant.REGEXP_ACCOUNT_NUMBER;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MasterAccountRequestDto {

    @NotBlank(message = "Номер аккаунта не может быть пустым")
    @Pattern(regexp = REGEXP_ACCOUNT_NUMBER, message = "Неккоректные данные номера счета")
    private String accountNumber;
}
