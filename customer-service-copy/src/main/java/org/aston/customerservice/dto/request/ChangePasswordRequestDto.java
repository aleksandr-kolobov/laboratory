package org.aston.customerservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.aston.customerservice.configuration.ApplicationConstant.*;

@Getter
@AllArgsConstructor
public class ChangePasswordRequestDto {

    @NotBlank(message = MESSAGE_NO_PARAMETER_AVAILABLE)
    private String oldPassword;

    @NotNull(message = MESSAGE_NO_PARAMETER_AVAILABLE)
    @Pattern(regexp = REGEXP_PASSWORD, message = MESSAGE_PASSWORD_DOES_NOT_SATISFY_REQUIREMENTS)
    private String newPassword;
}
