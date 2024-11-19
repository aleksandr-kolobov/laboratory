package org.aston.customerservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFullNameResponseDto {

    private String firstName;

    private String lastName;

    private String middleName;
}
