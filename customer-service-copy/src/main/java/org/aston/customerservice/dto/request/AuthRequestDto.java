package org.aston.customerservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import static org.aston.customerservice.configuration.ApplicationConstant.*;

@Getter
@Setter
@AllArgsConstructor
public class AuthRequestDto {

    @NotEmpty(message = MESSAGE_NO_PARAMETER_AVAILABLE)
    @Pattern(regexp = REGEXP_PHONE_NUMBER, message = MESSAGE_INCORRECT_DATA)
    private String phoneNumber;

    @NotEmpty(message = MESSAGE_NO_PARAMETER_AVAILABLE)
    @Pattern(regexp = REGEXP_PASSWORD, message = MESSAGE_PASSWORD_DOES_NOT_SATISFY_REQUIREMENTS)
    private String password;
}
