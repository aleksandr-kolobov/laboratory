package org.aston.customerservice.dto.response;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import static org.aston.customerservice.configuration.ApplicationConstant.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {

    private String firstName;

    private String lastName;

    private String middleName;

    @Pattern(regexp = REGEXP_PHONE_NUMBER, message = MESSAGE_INCORRECT_DATA)
    private String phoneNumber;

    @Pattern(regexp = REGEXP_EMAIL, message = MESSAGE_INCORRECT_DATA)
    private String email;

    @Pattern(regexp = REGEXP_DATE, message = MESSAGE_INCORRECT_DATA_OR_DATE_FORMAT)
    private String birthDate;

    private short childCount;

    @Pattern(regexp = REGEXP_DATE, message = MESSAGE_INCORRECT_DATA_OR_DATE_FORMAT)
    private String registrationDateBank;

    private String status;
}
