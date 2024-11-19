package org.aston.infoservice.persistent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "object")
public class InfoObject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "object_id")
    private UUID objectId;

    @NotNull
    @Column(name = "object_type_name")
    private String objectTypeName;

    @NotNull
    @Column(name = "object_number")
    private Integer objectNumber;

    @NotNull
    @Column(name = "region")
    private String region;

    @NotNull
    @Column(name = "location")
    private String location;

    @Column(name = "street")
    private String street;

    @Column(name = "microdistrict")
    private String microdistrict;

    @Column(name = "building_number_house")
    private String buildingNumberHouse;

    @NotNull
    @Column(name = "house_number")
    private String houseNumber;

    @NotNull
    @Column(name = "segment")
    private String segment;

    @NotNull
    @Column(name = "currency_exchange")
    private Boolean currencyExchange;

    @NotNull
    @Column(name = "replenishment")
    private Boolean replenishment;

    @NotNull
    @Column(name = "replenishment_without_card")
    private Boolean replenishmentWithoutCard;

    @NotNull
    @Column(name = "withdrawal")
    private Boolean withdrawal;

    @NotNull
    @Column(name = "bank_transfer")
    private Boolean bankTransfer;

    @NotNull
    @Column(name = "transfer_between_accounts")
    private Boolean transferBetweenAccounts;

    @NotNull
    @Column(name = "credit_take")
    private Boolean creditTake;

    @NotNull
    @Column(name = "credit_repayment")
    private Boolean creditRepayment;

    @NotNull
    @Column(name = "insurance")
    private Boolean insurance;

    @NotNull
    @Column(name = "object_status")
    private Boolean objectStatus;

    @NotNull
    @Column(name = "schedule")
    private String schedule;

    @NotNull
    @Column(name = "coordinates", columnDefinition = "geometry(Point, 4326)")
    private Point coordinates;
}
