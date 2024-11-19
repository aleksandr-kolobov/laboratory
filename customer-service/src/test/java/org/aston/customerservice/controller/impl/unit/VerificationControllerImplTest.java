package org.aston.customerservice.controller.impl.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aston.customerservice.controller.impl.VerificationControllerImpl;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.request.VerificationRequestDto;
import org.aston.customerservice.service.VerificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.aston.customerservice.data.AuthTestData.PHONE_NUMBER;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Unit тест для VerificationControllerImpl")
@ExtendWith(MockitoExtension.class)
public class VerificationControllerImplTest {

    @Mock
    private VerificationService verificationService;
    @InjectMocks
    private VerificationControllerImpl verificationControllerImpl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void givenPhoneNumberRequestDto_whenGenerateCode_thenReturnOK() throws Exception {
        PhoneNumberRequestDto dto = new PhoneNumberRequestDto(PHONE_NUMBER);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/customer/auth/verification/generate_code")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(verificationControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk());

        verify(verificationService, times(1)).sendCode(PHONE_NUMBER);
    }

    @Test
    void givenEmptyNumberRequestDto_whenGenerateCode_thenReturnBadRequest() throws Exception {
        PhoneNumberRequestDto dto = new PhoneNumberRequestDto("");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/customer/auth/verification/generate_code")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(verificationControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenPhoneNumberRequestDto_whenCheckCode_thenReturnOK() throws Exception {
        VerificationRequestDto dto = new VerificationRequestDto(PHONE_NUMBER, "123456");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/customer/auth/verification/check_code")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(verificationControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk());

        verify(verificationService, times(1)).checkCode(PHONE_NUMBER, "123456");
    }
}
