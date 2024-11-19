package org.aston.customerservice.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordEncoderMapperTest {

    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final PasswordEncoderMapper passwordEncoderMapper = new PasswordEncoderMapper(passwordEncoder);
    private final UserProfileMapper userProfileMapper = Mappers.getMapper(UserProfileMapper.class);

    @Test
    void givenValidPassword_whenEncodePassword_thenReturnEncodedPassword() {
        String password = "testPassword";
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        String result = passwordEncoderMapper.encode(password);

        assertEquals(encodedPassword, result);
    }
}
