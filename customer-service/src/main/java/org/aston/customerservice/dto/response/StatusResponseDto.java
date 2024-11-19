package org.aston.customerservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponseDto {

    private Short statusType;

    private String message;
}
