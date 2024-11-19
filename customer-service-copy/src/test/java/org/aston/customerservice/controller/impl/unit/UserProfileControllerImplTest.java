package org.aston.customerservice.controller.impl.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aston.customerservice.controller.impl.UserProfileControllerImpl;
import org.aston.customerservice.dto.request.ChangePasswordRequestDto;
import org.aston.customerservice.service.UserProfileService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тест для UserProfileControllerImpl")
public class UserProfileControllerImplTest {

    @Mock
    private UserProfileService userProfileService;
    @InjectMocks
    private UserProfileControllerImpl userProfileControllerImpl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void givenNewPassword_whenChangePassword_thenReturnOk() throws Exception {

        ChangePasswordRequestDto dto = new ChangePasswordRequestDto("Password@123", "Password@321");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/api/v1/customer/profile/change_password")
                .header("Customer-Phone", PHONE_NUMBER)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(userProfileControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void givenNewEmptyPassword_whenChangePassword_thenReturnBadRequest() throws Exception {
        ChangePasswordRequestDto dto = new ChangePasswordRequestDto("Password@123", "");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/api/v1/customer/profile/change_password")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(userProfileControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
}
