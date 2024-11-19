package org.aston.cardservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aston.cardservice.persistent.enums.CardStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "card_id")
    private UUID cardId;

    @NotNull
    @Column(name = "customer_id")
    private UUID customerId;

    @NotNull
    @Column(name = "account_number")
    private String accountNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_limit")
    private UserLimit userLimit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_product_id")
    private CardProduct cardProduct;

    @NotNull
    @Column(name = "card_number")
    private String cardNumber;

    @NotNull
    @Column(name = "balance")
    private BigDecimal balance;

    @NotNull
    @Column(name = "cvv_encrypted")
    private String cvvEncrypted;

    @NotNull
    @Column(name = "pin_code_hash")
    private String pinCodeHash;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_name")
    private CardStatus statusName;

    @NotNull
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "expiration_at")
    private OffsetDateTime expirationAt;

    @Column(name = "closed_at")
    private OffsetDateTime closedAt;

    @Column(name = "blocked_at")
    private OffsetDateTime blockedAt;

    @Column(name = "block_comment")
    private String blockComment;
}
