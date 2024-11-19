package org.aston.cardservice.persistent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "condition")
public class Condition {

    @Id
    @Column(name = "condition_id")
    private Short conditionId;

    @NotNull
    @Column(name = "condition_withdraw")
    private BigDecimal conditionWithdraw;

    @NotNull
    @Column(name = "condition_partner_withdraw")
    private BigDecimal conditionPartnerWithdraw;

    @NotNull
    @Column(name = "condition_world_withdraw")
    private BigDecimal conditionWorldWithdraw;

    @NotNull
    @Column(name = "condition_transaction")
    private BigDecimal conditionTransaction;

    @NotNull
    @Column(name = "condition_pay")
    private BigDecimal conditionPay;
}
