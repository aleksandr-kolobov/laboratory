package org.aston.customerservice.controller.impl.integration.configuration;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public class ContainerConfig {

    public static class PostgresSqlContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Container
        private static final PostgreSQLContainer<?> postgresqlContainer =
                new PostgreSQLContainer<>(DockerImageName.parse("postgres"));

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Startables.deepStart(postgresqlContainer).join();

            TestPropertyValues.of(
                    "spring.datasource.url=" + postgresqlContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgresqlContainer.getUsername(),
                    "spring.datasource.password=" + postgresqlContainer.getPassword()
            ).applyTo(applicationContext.getEnvironment());
        }
    }

    public static class RedisSqlContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Container
        private static final RedisContainer redisContainer =
                new RedisContainer(DockerImageName.parse("redis/redis-stack:latest"));

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Startables.deepStart(redisContainer).join();
            TestPropertyValues.of(
                    "spring.data.redis.host=" + redisContainer.getHost(),
                    "spring.data.redis.port=" + redisContainer.getMappedPort(6379).toString()
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
