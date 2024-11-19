package org.aston.customerservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

import static org.aston.customerservice.configuration.ApplicationConstant.REGEXP_PASSWORD;
import static org.aston.customerservice.configuration.ApplicationConstant.MESSAGE_NO_PARAMETER_AVAILABLE;
import static org.aston.customerservice.configuration.ApplicationConstant.MESSAGE_PASSWORD_DOES_NOT_SATISFY_REQUIREMENTS;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileCreationRequestDto {

    @NotNull(message = MESSAGE_NO_PARAMETER_AVAILABLE)
    private UUID customerId;

    @NotEmpty(message = MESSAGE_NO_PARAMETER_AVAILABLE)
    @Pattern(regexp = REGEXP_PASSWORD, message = MESSAGE_PASSWORD_DOES_NOT_SATISFY_REQUIREMENTS)
    private String password;
}
