package org.aston.cardservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank_limit")
public class BankLimit {

    @Id
    @Column(name = "limit_id")
    private Short limitId;

    @OneToMany(mappedBy = "bankLimit")
    private List<CardProduct> cardProducts;

    @NotNull
    @Column(name = "withdraw_day")
    private BigDecimal withdrawDay;

    @NotNull
    @Column(name = "withdraw_month")
    private BigDecimal withdrawMonth;

    @NotNull
    @Column(name = "transaction_day")
    private BigDecimal transactionDay;

    @NotNull
    @Column(name = "transaction_month")
    private BigDecimal transactionMonth;

    @NotNull
    @Column(name = "pay_day")
    private BigDecimal payDay;

    @NotNull
    @Column(name = "pay_month")
    private BigDecimal payMonth;

    @NotNull
    @Column(name = "over_withdraw_day")
    private BigDecimal overWithdrawDay;

    @NotNull
    @Column(name = "over_withdraw_month")
    private BigDecimal overWithdrawMonth;

    @NotNull
    @Column(name = "over_transaction_day")
    private BigDecimal overTransactionDay;

    @NotNull
    @Column(name = "over_transaction_month")
    private BigDecimal overTransactionMonth;

    @NotNull
    @Column(name = "over_pay_day")
    private BigDecimal overPayDay;

    @NotNull
    @Column(name = "over_pay_month")
    private BigDecimal overPayMonth;
}
