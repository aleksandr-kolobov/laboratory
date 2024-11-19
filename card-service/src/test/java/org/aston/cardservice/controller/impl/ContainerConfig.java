package org.aston.cardservice.controller.impl;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MinIOContainer;
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

    public static class MinioContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            String accessKey = applicationContext.getEnvironment().getProperty("minio.accessKey");
            String secretKey = applicationContext.getEnvironment().getProperty("minio.secretKey");

            MinIOContainer minioContainer =
                    new MinIOContainer("minio/minio")
                            .withEnv("MINIO_ACCESS_KEY", accessKey)
                            .withEnv("MINIO_SECRET_KEY", secretKey);

            minioContainer.start();

            TestPropertyValues.of(
                    "minio.url=" + minioContainer.getS3URL(),
                    "minio.accessKey=" + accessKey,
                    "minio.secretKey=" + secretKey
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
