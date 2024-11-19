package org.aston.customerservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "identity_document")
public class IdentityDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "document_id")
    private UUID documentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id")
    private IdentityDocumentType documentType;

    @NotNull
    @Column(name = "document_number")
    private String documentNumber;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issuing_country")
    private Country country;

    @NotNull
    @Column(name = "issuing_authority")
    private String issuing_authority;

    @Column(name = "code_issuing_authority")
    private String code_issuing_authority;

    @NotNull
    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;
}
