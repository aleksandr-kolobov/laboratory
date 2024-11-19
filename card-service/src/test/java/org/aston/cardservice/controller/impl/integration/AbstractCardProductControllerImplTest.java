package org.aston.cardservice.controller.impl.integration;

import org.aston.cardservice.controller.impl.ContainerConfig;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.discovery.enabled=false"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {ContainerConfig.MinioContainerInitializer.class, ContainerConfig.PostgresSqlContainerInitializer.class})
@Sql({"classpath:sql/initData.sql"})
abstract  class AbstractCardProductControllerImplTest {

}
