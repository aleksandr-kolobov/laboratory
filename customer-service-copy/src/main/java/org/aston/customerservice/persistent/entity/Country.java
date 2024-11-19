package org.aston.customerservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "country_dictionary")
public class Country {

    @Id
    @Column(name = "country_code_iso")
    private String countryCode;

    @NotNull
    @Column(name = "country_name", nullable = false)
    private String countryName;
}
