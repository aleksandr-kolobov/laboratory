package org.aston.customerservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "family_status_dictionary")
public class FamilyStatus {

    @Id
    @Column(name = "family_status")
    private Short familyStatus;

    @NotNull
    @Column(name = "family_status_name")
    private String status;
}
