package org.aston.customerservice.controller.impl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aston.customerservice.controller.impl.integration.configuration.ContainerConfig;
import org.aston.customerservice.dto.request.PasswordRecoveryRequestDto;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.exception.UserProfileException;
import org.aston.customerservice.service.CustomerService;
import org.aston.customerservice.service.StatusService;
import org.aston.customerservice.service.UserProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.discovery.enabled=false"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {ContainerConfig.PostgresSqlContainerInitializer.class})
@Sql({"classpath:sql/initData.sql"})
public class RegistryControllerImplIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    private CustomerService customerService;
    @SpyBean
    private StatusService statusService;
    @SpyBean
    private UserProfileService userProfileService;

    @Test
    @DisplayName("Тест метода passwordRecovery, когда пароль успешно изменен")
    public void givenValidRequest_whenPasswordRecovery_thenPasswordIsUpdated() throws Exception {
        String newPassword = "Java123!";
        String uuid = "7ccbd32e-cdd7-490c-9446-dd716d236fc5";
        PasswordRecoveryRequestDto requestDto = new PasswordRecoveryRequestDto(newPassword, uuid);
        String dtoToJson = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(post("/api/v1/customer/registry/recovery_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoToJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Тест метода passwordRecovery, когда пароли совпадают")
    void givenMatchingPasswords_whenPasswordRecovery_thenRepeatablePasswordExceptionThrown() throws Exception {
        String repeatablePassword = "Java122_";
        String uuid = "7ccbd32e-cdd7-490c-9446-dd716d236fc5";
        PasswordRecoveryRequestDto requestDto = new PasswordRecoveryRequestDto(repeatablePassword, uuid);
        String dtoToJson = objectMapper.writeValueAsString(requestDto);
        doThrow(new UserProfileException(HttpStatus.CONFLICT, "Данный пароль использовался ранее"))
                .when(userProfileService).recoveryPassword(requestDto);
        mockMvc.perform(post("/api/v1/customer/registry/recovery_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoToJson))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string("Данный пароль использовался ранее"));
    }

    @Test
    public void testGetCustomerStatusType_thenStatusType1() throws Exception {
        PhoneNumberRequestDto phoneNumberRequestDto = new PhoneNumberRequestDto("79234251422");
        String dtoToJson = objectMapper.writeValueAsString(phoneNumberRequestDto);
        mockMvc.perform(post("/api/v1/customer/registry/check_status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoToJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusType").value(1))
                .andExpect(jsonPath("$.message").value("Без ограничений"));
    }

    @Test
    public void testGetCustomerStatusType_thenStatusType2() throws Exception {
        PhoneNumberRequestDto phoneNumberRequestDto = new PhoneNumberRequestDto("79234251313");
        String dtoToJson = objectMapper.writeValueAsString(phoneNumberRequestDto);
        mockMvc.perform(post("/api/v1/customer/registry/check_status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoToJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusType").value(2))
                .andExpect(jsonPath("$.message")
                        .value("Ограничение прав доступа на управление СДБО"));
    }
}