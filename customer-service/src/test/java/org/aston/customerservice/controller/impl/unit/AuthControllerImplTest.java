package org.aston.customerservice.controller.impl.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aston.customerservice.controller.impl.AuthControllerImpl;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тест для AuthControllerImpl")
public class AuthControllerImplTest {
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AuthControllerImpl authControllerImpl;

    @Test
    void givenInvalidPhoneNumber_whenCheckRegistration_thenReturnBadRequest() throws Exception {

        PhoneNumberRequestDto request = new PhoneNumberRequestDto();
        request.setPhoneNumber("6625550144");

        String content = (new ObjectMapper()).writeValueAsString(request);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/customer/auth/check_registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authControllerImpl)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenValidPhoneNumber_whenCheckRegistration_thenReturnOk() throws Exception {

        PhoneNumberRequestDto request = new PhoneNumberRequestDto();
        request.setPhoneNumber("79999999999");
        String content = (new ObjectMapper()).writeValueAsString(request);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/customer/auth/check_registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(authControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
