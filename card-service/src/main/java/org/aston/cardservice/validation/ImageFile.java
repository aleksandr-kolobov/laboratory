package org.aston.cardservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static org.aston.cardservice.configuration.ApplicationConstant.MULTIPART_FORM_DATA_FAILURE_DEFAULT_MSG;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageFileValidator.class)
@Documented
public @interface ImageFile {

    String message() default MULTIPART_FORM_DATA_FAILURE_DEFAULT_MSG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
