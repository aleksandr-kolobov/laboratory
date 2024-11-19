package org.aston.customerservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address_type_dictionary")
public class AddressType {

    @Id
    @Column(name = "address_type_id")
    private Short addressTypeId;

    @NotNull
    @Column(name = "address_type")
    private String addressType;
}
