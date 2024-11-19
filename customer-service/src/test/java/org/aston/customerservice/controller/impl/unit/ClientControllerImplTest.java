package org.aston.customerservice.controller.impl.unit;

import org.apache.http.HttpHeaders;
import org.aston.customerservice.controller.impl.ClientControllerImpl;
import org.aston.customerservice.dto.response.CustomerFullNameResponseDto;
import org.aston.customerservice.service.CustomerService;
import org.aston.customerservice.service.StatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.aston.customerservice.configuration.ApplicationConstant.HEADER_KEY_CUSTOMER_ID;
import static org.aston.customerservice.configuration.ApplicationConstant.TOKEN_PREFIX;
import static org.aston.customerservice.data.AuthTestData.ACCESS_TOKEN;
import static org.aston.customerservice.data.AuthTestData.CUSTOMER_ID;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тест для ClientControllerImpl")
public class ClientControllerImplTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private StatusService statusService;

    @InjectMocks
    private ClientControllerImpl clientController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    @DisplayName("Тест успешного получения полного имени клиента")
    void givenCustomerId_whenGetCustomerFullName_thenReturnFullNameDto() throws Exception {
        String customerId = "7ccbd32e-cdd7-490c-9446-dd716d236fc5";
        CustomerFullNameResponseDto expectedResponse =
                new CustomerFullNameResponseDto("Иван", "Иванов", "Иванович");

        when(customerService.getFullName(customerId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/customer/full_name")
                        .header("Customer-Id", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(expectedResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedResponse.getLastName()))
                .andExpect(jsonPath("$.middleName").value(expectedResponse.getMiddleName()));

        verify(customerService, times(1)).getFullName(customerId);
    }

    @Test
    void givenCustomerId_whenGetStatusTypeById_thenReturnDto() throws Exception {
        String mockToken = TOKEN_PREFIX + ACCESS_TOKEN;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/customer/status_type")
                .header(HttpHeaders.AUTHORIZATION, mockToken)
                .header(HEADER_KEY_CUSTOMER_ID, CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
