package org.aston.customerservice.controller.impl.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aston.customerservice.controller.impl.CustomerControllerImpl;
import org.aston.customerservice.dto.request.EmailRequestDto;
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
import static org.aston.customerservice.data.AuthTestData.ACCESS_TOKEN;
import static org.aston.customerservice.data.AuthTestData.TOKEN_PREFIX;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тест для CustomerControllerImpl")
public class CustomerControllerImplTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerControllerImpl customerController;

    @Test
    void givenSuccessMessage_whenSuccessChangeEmail_thenReturnOK() throws Exception {

        String mockToken = TOKEN_PREFIX + ACCESS_TOKEN;
        String phoneNumber = "123456789";
        EmailRequestDto newEmail = new EmailRequestDto();
        newEmail.setEmail("newemail@test.com");

        String content = (new ObjectMapper()).writeValueAsString(newEmail);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/api/v1/customer/settings/new_email")
                .header("Customer-Phone", "123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(customerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(customerService, times(1)).updateEmail(eq(newEmail), eq(phoneNumber));
    }

    @Test
    void givenInvalidPhoneNumber_whenCheckChangeEmail_thenReturnBadRequest() throws Exception {

        EmailRequestDto newEmail = new EmailRequestDto();
        newEmail.setEmail("newe@mail@test.com");
        String content = (new ObjectMapper()).writeValueAsString(newEmail);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/api/v1/customer/settings/new_email")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .header("customerPhone", "123456789")
                .header("Authorization", TOKEN_PREFIX + "test_token");

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(customerController)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenNewEmail_whenAddEmail_thenReturnOK() throws Exception {

        String mockToken = TOKEN_PREFIX + ACCESS_TOKEN;
        String phoneNumber = "123456789";
        EmailRequestDto newEmail = new EmailRequestDto();
        newEmail.setEmail("newemail@test.com");

        String content = (new ObjectMapper()).writeValueAsString(newEmail);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/customer/settings/add_email")
                .header("Customer-Phone", "123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(customerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(customerService, times(1)).addEmail(eq(newEmail), eq(phoneNumber));
    }

}
