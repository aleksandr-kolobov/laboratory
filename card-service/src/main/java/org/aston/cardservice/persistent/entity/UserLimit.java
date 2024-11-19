package org.aston.cardservice.persistent.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_limit")
public class UserLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_limit_id")
    private UUID userLimitId;

    @OneToOne(mappedBy = "userLimit")
    private Card card;

    @NotNull
    @Column(name = "withdraw_limit_day")
    private BigDecimal withdrawLimitDay;

    @NotNull
    @Column(name = "withdraw_limit_month")
    private BigDecimal withdrawLimitMonth;

    @NotNull
    @Column(name = "transaction_limit_day")
    private BigDecimal transactionLimitDay;

    @NotNull
    @Column(name = "transaction_limit_month")
    private BigDecimal transactionLimitMonth;

    @NotNull
    @Column(name = "pay_limit_day")
    private BigDecimal payLimitDay;

    @NotNull
    @Column(name = "pay_limit_month")
    private BigDecimal payLimitMonth;
}
