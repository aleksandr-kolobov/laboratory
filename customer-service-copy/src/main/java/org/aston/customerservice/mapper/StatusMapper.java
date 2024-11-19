package org.aston.customerservice.mapper;

import org.aston.customerservice.dto.response.StatusTypeResponseDto;
import org.aston.customerservice.persistent.entity.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatusMapper {

    @Mapping(target = "statusType", source = "status.statusType")
    StatusTypeResponseDto statusTypeToDto(Status status);
}
