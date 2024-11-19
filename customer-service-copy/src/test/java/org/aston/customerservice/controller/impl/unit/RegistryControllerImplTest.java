package org.aston.customerservice.controller.impl.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aston.customerservice.dto.response.CustomerIdResponseDto;
import org.aston.customerservice.dto.request.UserProfileCreationRequestDto;
import org.aston.customerservice.service.CustomerService;
import org.aston.customerservice.service.UserProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.aston.customerservice.controller.impl.RegistryControllerImpl;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.response.StatusResponseDto;
import org.aston.customerservice.service.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

import static org.aston.customerservice.data.AuthTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тест для RegistryControllerImpl")
class RegistryControllerImplTest {

    @Mock
    private CustomerService customerService;
    @Mock
    private StatusService statusService;
    @Mock
    private UserProfileService userProfileService;
    @InjectMocks
    private RegistryControllerImpl registryControllerImpl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetCustomerStatusType_thenStatusTypeReturned() {
        PhoneNumberRequestDto phoneNumberRequestDto = new PhoneNumberRequestDto(PHONE_NUMBER);
        StatusResponseDto mockStatus = new StatusResponseDto();
        mockStatus.setStatusType((short) 1);
        when(statusService.getCustomerStatus(phoneNumberRequestDto)).thenReturn(mockStatus);

        ResponseEntity<StatusResponseDto> responseEntity = registryControllerImpl.getCustomerStatusType(phoneNumberRequestDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        StatusResponseDto responseStatusDto = responseEntity.getBody();
        assert responseStatusDto != null;
        assertEquals((short) 1, responseStatusDto.getStatusType());
        verify(statusService, times(1)).getCustomerStatus(phoneNumberRequestDto);
    }

    @Test
    void testGetCustomerStatusType_thenReturnsStatusType2WithMessage() {
        PhoneNumberRequestDto phoneNumberRequestDto = new PhoneNumberRequestDto(PHONE_NUMBER);
        StatusResponseDto mockStatus = new StatusResponseDto();
        mockStatus.setStatusType((short) 2);
        mockStatus.setMessage("Ограничение прав доступа на управление СДБО");
        when(statusService.getCustomerStatus(phoneNumberRequestDto)).thenReturn(mockStatus);

        ResponseEntity<StatusResponseDto> responseEntity = registryControllerImpl.getCustomerStatusType(phoneNumberRequestDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        StatusResponseDto statusResponseDto = responseEntity.getBody();
        assert statusResponseDto != null;
        assertEquals((short) 2, statusResponseDto.getStatusType());
        assertEquals(mockStatus.getMessage(), statusResponseDto.getMessage());
        verify(statusService, times(1)).getCustomerStatus(phoneNumberRequestDto);
    }

    @Test
    void testGetCustomerStatusType_thenReturnsStatusType3WithMessage() {
        PhoneNumberRequestDto phoneNumberRequestDto = new PhoneNumberRequestDto(PHONE_NUMBER);
        StatusResponseDto mockStatus = new StatusResponseDto();
        mockStatus.setStatusType((short) 3);
        mockStatus.setMessage("Запрещено. Отсутствуют права доступа к содержимому");
        when(statusService.getCustomerStatus(phoneNumberRequestDto)).thenReturn(mockStatus);

        ResponseEntity<StatusResponseDto> responseEntity = registryControllerImpl.getCustomerStatusType(phoneNumberRequestDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        StatusResponseDto statusResponseDto = responseEntity.getBody();
        assert statusResponseDto != null;
        assertEquals((short) 3, statusResponseDto.getStatusType());
        assertEquals(mockStatus.getMessage(), statusResponseDto.getMessage());
        verify(statusService, times(1)).getCustomerStatus(phoneNumberRequestDto);
    }

    @Test
    void givenInvalidPhoneNumber_whenCheckMissRegistration_thenReturnBadRequest() throws Exception {

        PhoneNumberRequestDto request = new PhoneNumberRequestDto();
        request.setPhoneNumber("6625550144");

        String content = (new ObjectMapper()).writeValueAsString(request);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/customer/registry/check_miss_registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(registryControllerImpl)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenValidPhoneNumber_whenCheckMissRegistration_thenReturnOk() throws Exception {

        CustomerIdResponseDto customerIdDto = new CustomerIdResponseDto();
        customerIdDto.setCustomerId("565004f8-1b7a-4383-8e04-7cf97e2b796e");
        when(customerService.checkMissRegistration(Mockito.any())).thenReturn(customerIdDto);

        PhoneNumberRequestDto request = new PhoneNumberRequestDto();
        request.setPhoneNumber("79999999999");
        String content = (new ObjectMapper()).writeValueAsString(request);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/customer/registry/check_miss_registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(registryControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"customerId\":\"565004f8-1b7a-4383-8e04-7cf97e2b796e\"}"));
    }

    @Test
    void givenValidRequest_whenCreateUserProfile_thenReturnCreated() throws Exception {

        UserProfileCreationRequestDto request =
                new UserProfileCreationRequestDto(UUID.fromString(CUSTOMER_ID), PASSWORD);

        String content = objectMapper.writeValueAsString(request);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/customer/registry/create_user_profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(registryControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(userProfileService, times(1)).createUserProfile(eq(request));
    }

    @Test
    void givenInvalidRequest_whenCreateUserProfile_thenReturnBadRequest() throws Exception {

        UserProfileCreationRequestDto personalAccountCreationRequestDto =
                new UserProfileCreationRequestDto(UUID.randomUUID(), "PASSWORD");

        String content = objectMapper.writeValueAsString(personalAccountCreationRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/customer/registry/create_user_profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(registryControllerImpl)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(status().isBadRequest());
    }
}