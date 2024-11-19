package org.aston.customerservice.controller.impl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aston.customerservice.controller.impl.integration.configuration.ContainerConfig;
import org.aston.customerservice.service.CustomerService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.discovery.enabled=false"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {ContainerConfig.PostgresSqlContainerInitializer.class})
@Sql({"classpath:sql/initData.sql"})
public class CustomerControllerImplIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    private CustomerService customerService;

    @Test
    public void givenPhoneNumber_whenFindCustomerInfo_thenCustomerResponse() throws Exception {
        mockMvc.perform(get("/api/v1/customer/settings/info")
                        .header("Customer-Phone", "79234251422")
                        .header("Customer-Status", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
