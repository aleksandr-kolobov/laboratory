package org.aston.customerservice.controller.impl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aston.customerservice.controller.impl.integration.configuration.ContainerConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.discovery.enabled=false"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {ContainerConfig.PostgresSqlContainerInitializer.class})
@Sql({"classpath:sql/initData.sql"})
public class VerificationControllerImplIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Тест метода whenGenerationCode")
    public void givenPhoneNumber_whenGenerationCode_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customer/auth/verification/generate_code")
                        .content("""
                              {
                                  "phoneNumber": "79234251422"
                              }
                            """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест метода whenCheckCode")
    public void givenPhoneNumberAndCode_whenCheckCode_thenOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customer/auth/verification/check_code")
                        .content("""
                              {
                                  "phoneNumber": "79234251422",
                                  "code": "123456"
                              }
                            """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
