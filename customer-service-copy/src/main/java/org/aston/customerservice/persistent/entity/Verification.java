package org.aston.customerservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verification")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_verification_id")
    private UUID userVerificationId;

    @NotNull
    @Column(name = "customer_id")
    private UUID customerId;

    @NotNull
    @Column(name = "verification_code", columnDefinition = "bpchar")
    private String verificationCode;

    @NotNull
    @Column(name = "code_expiration")
    private Instant codeExpiration;

    @NotNull
    @Column(name = "wrong_attempts_counter")
    private short wrongAttemptsCounter;

    @NotNull
    @Column(name = "sms_block_sending")
    private Instant smsBlockSending;

    public void incrementAttemptsCounter() {
        wrongAttemptsCounter++;
    }

    public void resetWrongAttemptsCounter() {
        wrongAttemptsCounter = 0;
    }
}
