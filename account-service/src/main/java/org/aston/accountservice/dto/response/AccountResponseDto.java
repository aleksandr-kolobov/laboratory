package org.aston.accountservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {

    private String accountNumber;

    private String nameAccount;

    private Boolean masterAccount;

    private String type;

    private String currencyName;

    private String statusName;

    private BigDecimal accountBalance;

    private LocalDate createdAt;

    private LocalDate closedAt;

    private String blockReason;

    private String blockComment;
}
