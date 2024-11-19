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
public class PasswordRecoveryRequestDto {

    @NotEmpty(message = MESSAGE_NO_PARAMETER_AVAILABLE)
    @Pattern(regexp = REGEXP_PASSWORD, message = MESSAGE_PASSWORD_DOES_NOT_SATISFY_REQUIREMENTS)
    private String password;

    @NotEmpty(message = MESSAGE_NO_PARAMETER_AVAILABLE)
    @Pattern(regexp = REGEXP_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT)
    private String customerId;
}