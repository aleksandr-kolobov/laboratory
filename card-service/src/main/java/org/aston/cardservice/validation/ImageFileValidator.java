package org.aston.cardservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import static org.aston.cardservice.configuration.ApplicationConstant.*;

public class ImageFileValidator implements ConstraintValidator<ImageFile, MultipartFile> {

    @Override
    public void initialize(ImageFile constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(FILE_EMPTY_MSG).addConstraintViolation();
            return false;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(FILE_NAME_EMPTY_MESSAGE).addConstraintViolation();
            return false;
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(FILE_EXTENSION_DELIMITER) + 1);
        if (!SUPPORTED_IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(FILE_NOT_IMAGE_MSG).addConstraintViolation();
            return false;
        }
        return true;
    }
}
