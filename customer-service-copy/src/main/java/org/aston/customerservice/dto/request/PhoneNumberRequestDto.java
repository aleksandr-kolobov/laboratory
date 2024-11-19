package org.aston.customerservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.aston.customerservice.configuration.ApplicationConstant.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumberRequestDto {

    @NotEmpty(message = MESSAGE_NO_PARAMETER_AVAILABLE)
    @Pattern(regexp = REGEXP_PHONE_NUMBER, message = MESSAGE_INCORRECT_DATA)
    private String phoneNumber;
}