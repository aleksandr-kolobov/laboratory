package org.aston.customerservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.request.VerificationRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.aston.customerservice.dto.request.EmailRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeEach
    void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    void close() {
        validatorFactory.close();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ran87878-dom@random.com",
            "random@ran-dom.com",
            "ra.nd8989om@random.dot",
            "random.random@email.com"})
    void validEmailTest(String email) {
        EmailRequestDto emailDto = new EmailRequestDto(email);
        Set<ConstraintViolation<EmailRequestDto>> violations =
                validator.validate(emailDto);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "random@random.email.com",
            "random.email.com@",
            "random email.com@",
            "@random.email.com",
            "random.email.com"})
    void invalidEmailTest(String email) {
        EmailRequestDto emailDto = new EmailRequestDto(email);
        Set<ConstraintViolation<EmailRequestDto>> violations =
                validator.validate(emailDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void invalidEmail_EmailIsEmpty() {
        EmailRequestDto emailDto = new EmailRequestDto("");
        Set<ConstraintViolation<EmailRequestDto>> violations =
                validator.validate(emailDto);
        assertEquals(2, violations.size());
    }

    @ParameterizedTest
    @CsvSource({
            "79234251422, 123456",
            "79234251422, 16",
            "79234251422, 1234",
            "79234251422, 3",
    })
    void validVerificationRequestDto(String phoneNumber, String code) {
        VerificationRequestDto verificationRequestDto =
                new VerificationRequestDto(phoneNumber, code);
        Set<ConstraintViolation<VerificationRequestDto>> violations =
                validator.validate(verificationRequestDto);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "79234251422, ''",
            "9234251422, 123456",
            "89234251422, 16",
            "+79234251422, 1234"
    })
    void invalidVerificationRequestDto(String phoneNumber, String code) {
        VerificationRequestDto verificationRequestDto =
                new VerificationRequestDto(phoneNumber, code);
        Set<ConstraintViolation<VerificationRequestDto>> violations =
                validator.validate(verificationRequestDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void invalidVerificationRequestDto_twoMistakes() {
        String phoneNumber = "+79234251422";
        String code = " ";
        VerificationRequestDto verificationRequestDto =
                new VerificationRequestDto(phoneNumber, code);
        Set<ConstraintViolation<VerificationRequestDto>> violations =
                validator.validate(verificationRequestDto);
        assertEquals(2, violations.size());
    }

    @Test
    void validPhoneNumberRequestDto() {
        String phoneNumber = "79234251422";
        PhoneNumberRequestDto phoneNumberRequestDto =
                new PhoneNumberRequestDto(phoneNumber);
        Set<ConstraintViolation<PhoneNumberRequestDto>> violations =
                validator.validate(phoneNumberRequestDto);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "79234251422305",
            "+79234251422",
            "9234251422",
            "89234251422",
            "lsdfkjg"})
    void invalidPhoneNumberRequestDto(String phoneNumber) {
        PhoneNumberRequestDto phoneNumberRequestDto =
                new PhoneNumberRequestDto(phoneNumber);
        Set<ConstraintViolation<PhoneNumberRequestDto>> violations =
                validator.validate(phoneNumberRequestDto);
        assertFalse(violations.isEmpty());
    }
}
