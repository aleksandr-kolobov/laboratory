package org.aston.customerservice.controller.impl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aston.customerservice.controller.impl.integration.configuration.ContainerConfig;
import org.aston.customerservice.dto.request.ChangePasswordRequestDto;
import org.aston.customerservice.service.CustomerService;
import org.aston.customerservice.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.discovery.enabled=false"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {ContainerConfig.PostgresSqlContainerInitializer.class})
@Sql({"classpath:sql/initData.sql"})
public class UserProfileControllerImplIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    private CustomerService customerService;
    @SpyBean
    private UserProfileService userProfileService;

    @Test
    public void givenChangePasswordRequestDto_whenChangePassword_thenResponseOK() throws Exception {

        String oldPassword = "Password@123";
        String newPassword = "Password@321";
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto(oldPassword, newPassword);
        String dtoToJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(patch("/api/v1/customer/profile/change_password")
                        .header("Customer-Phone", "79234251422")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoToJson))
                .andExpect(status().isOk());
    }
}