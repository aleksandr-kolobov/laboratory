package org.aston.accountservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAccountsListResponseDto {

    private String accountNumber;

    private String type;

    private BigDecimal accountBalance;

    private String statusName;

    private String currencyName;

    private Boolean masterAccount;

    private String nameAccount;
}
