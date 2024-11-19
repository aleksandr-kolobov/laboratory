package org.aston.customerservice.mapper;

import org.aston.customerservice.annotation.EncodedMapping;
import org.aston.customerservice.dto.request.UserProfileCreationRequestDto;
import org.aston.customerservice.persistent.entity.Customer;
import org.aston.customerservice.persistent.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserProfileMapper {

    @Mapping(target = "userProfileId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "hashPassword", source = "request.password", qualifiedBy = EncodedMapping.class)
    @Mapping(target = "registrationDateApp", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "roles", ignore = true)
    UserProfile userProfileCreationDtoToEntity(UserProfileCreationRequestDto request, Customer customer);
}
