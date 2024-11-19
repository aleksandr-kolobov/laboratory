package org.aston.cardservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.aston.cardservice.persistent.enums.CardProductLevel;
import org.aston.cardservice.persistent.enums.CardProductType;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "card_product")
public class CardProduct {

    @Id
    @Column(name = "card_product_id")
    private String cardProductId;

    @OneToOne(mappedBy = "cardProduct", fetch = FetchType.LAZY)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_id")
    private BankLimit bankLimit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id")
    private Condition condition;

    @NotNull
    @Column(name = "image")
    private String image;

    @NotNull
    @Column(name = "name_product")
    private String nameProduct;

    @NotNull
    @Column(name = "fee_use")
    private BigDecimal feeUse;

    @NotNull
    @Column(name = "payment_system")
    private String paymentSystem;

    @NotNull
    @Column(name = "is_virtual")
    private Boolean isVirtual;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_card")
    private CardProductType typeCard;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private CardProductLevel level;
}
