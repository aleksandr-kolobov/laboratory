package org.aston.customerservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {

    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;

    @ManyToMany(mappedBy = "addresses")
    private Set<Customer> customers;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_type_id")
    private AddressType addressType;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_code_iso")
    private Country country;

    private String region;

    @NotNull
    private String location;

    @NotNull
    private String street;

    @NotNull
    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "building_number_house")
    private String buildingNumber;

    @Column(name = "apartment_number")
    private String apartmentNumber;

    @NotNull
    private String postcode;
}
