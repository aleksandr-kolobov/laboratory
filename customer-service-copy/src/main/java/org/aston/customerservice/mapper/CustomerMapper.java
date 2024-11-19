package org.aston.customerservice.mapper;

import org.aston.customerservice.dto.response.CustomerIdResponseDto;
import org.aston.customerservice.dto.response.CustomerResponseDto;
import org.aston.customerservice.dto.response.CustomerFullNameResponseDto;
import org.aston.customerservice.dto.response.CustomerIdResponseDto;
import org.aston.customerservice.dto.response.CustomerResponseDto;
import org.aston.customerservice.persistent.entity.Customer;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    @Mapping(source = "status.status", target = "status")
    CustomerResponseDto toCustomerResponseDto(Customer customer);

    @Mapping(source = "customer.customerId", target = "customerId")
    CustomerIdResponseDto toCustomerIdDto(Customer customer);

    @Mapping(source = "customer.firstName", target = "firstName")
    @Mapping(source = "customer.lastName", target = "lastName")
    @Mapping(source = "customer.middleName", target = "middleName")
    CustomerFullNameResponseDto toCustomerFullNameResponseDto(Customer customer);
}
