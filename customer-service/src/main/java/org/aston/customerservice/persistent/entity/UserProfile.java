package org.aston.customerservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.aston.customerservice.security.Role;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_profile_id")
    private UUID userProfileId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    @Column(name = "hash_password")
    private String hashPassword;

    @NotNull
    @Column(name = "registration_date_app")
    private LocalDate registrationDateApp;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_profile_id"))
    private Set<Role> roles = new HashSet<>();
}