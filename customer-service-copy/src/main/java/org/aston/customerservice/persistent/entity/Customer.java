package org.aston.customerservice.persistent.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customer_id")
    private UUID customerId;

    @OneToOne(mappedBy = "customer")
    private UserProfile userProfile;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "customer_address",
            joinColumns = {@JoinColumn(name = "customer_id")},
            inverseJoinColumns = {@JoinColumn(name = "address_id")})
    private Set<Address> addresses;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private List<IdentityDocument> documents;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "gender")
    private Character gender;

    @NotNull
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_status_code")
    private FamilyStatus familyStatus;

    @NotNull
    @Column(name = "child_count")
    private short childCount;

    @NotNull
    @Column(name = "registration_date_bank")
    private LocalDate registrationDateBank;

    @NotNull
    @Column(name = "secret_question")
    private String secretQuestion;

    @NotNull
    @Column(name = "secret_answer")
    private String secretAnswer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizenship")
    private Country country;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_type")
    private Status status;

    @NotNull
    @Column(name = "sms_notification")
    private boolean smsNotification;
}