package org.aston.customerservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "status_dictionary")
public class Status {

    @Id
    @Column(name = "status_type")
    private Short statusType;

    @NotNull
    @Column(name = "status_name")
    private String status;
}
