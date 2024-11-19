package org.aston.accountservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    private UUID accountId;

    @NotNull
    @Column(name = "customer_id")
    private UUID customerId;

    @NotNull
    @Column(name = "account_number")
    private String accountNumber;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotNull
    @Column(name = "account_balance")
    private BigDecimal accountBalance;

    @Column(name = "hold_balance")
    private BigDecimal holdBalance;

    @NotNull
    @Column(name = "status_name")
    private String statusName;

    @NotNull
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "closed_at")
    private LocalDate closedAt;

    @Column(name = "block_reason")
    private String blockReason;

    @Column(name = "block_comment")
    private String blockComment;

    @NotNull
    @Column(name = "currency_name")
    private String currencyName;

    @NotNull
    @Column(name = "master_account")
    private Boolean masterAccount;

    @Column(name = "name_account")
    private String nameAccount;
}
