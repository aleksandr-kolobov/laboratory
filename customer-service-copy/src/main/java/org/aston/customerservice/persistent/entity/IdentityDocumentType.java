package org.aston.customerservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "identity_document_type_dictionary")
public class IdentityDocumentType {

    @Id
    @Column(name = "document_type_id")
    private Short documentTypeId;

    @NotNull
    @Column(name = "document_type")
    private String documentType;
}
