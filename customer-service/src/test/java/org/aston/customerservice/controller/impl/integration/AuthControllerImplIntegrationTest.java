package org.aston.customerservice.controller.impl.integration;

import org.aston.customerservice.controller.impl.AuthControllerImpl;
import org.aston.customerservice.controller.impl.integration.configuration.ContainerConfig;
import org.aston.customerservice.security.service.JwtService;
import org.aston.customerservice.service.AuthService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Duration;

import static com.jayway.jsonpath.JsonPath.read;
import static org.aston.customerservice.data.AuthTestData.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.discovery.enabled=false"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {ContainerConfig.PostgresSqlContainerInitializer.class, ContainerConfig.RedisSqlContainerInitializer.class})
@Sql({"classpath:sql/initData.sql"})
class AuthControllerImplIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @SpyBean
    JwtService jwtService;
    @Autowired
    AuthControllerImpl authController;
    @Autowired
    AuthService authService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @SpyBean
    AuthenticationManager authenticationManager;
    @Autowired
    StringRedisTemplate redisTemplate;

    private String refreshToken;

    private Duration jwtRefreshExpiration = Duration.ofMinutes(2);

    @Value("${jwt.secret}")
    private String secret;

    @BeforeAll
    public void init() {
        refreshToken = createToken(PHONE_NUMBER, secret, jwtRefreshExpiration.toMillis());
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class FirstNestedClass {

        @BeforeAll()
        void beforeAll() {
            redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + PHONE_NUMBER, refreshToken, jwtRefreshExpiration);
        }

        @AfterAll()
        void afterAll() {
            redisTemplate.delete(REDIS_KEY_PREFIX + PHONE_NUMBER);
        }

        @Test
        void givenRefreshToken_whenRefreshToken_thenAccessAndRefreshTokens() throws Exception {
            String resultByUpdateToken = mockMvc
                    .perform(post("/api/v1/customer/auth/refresh_token").header(REFRESH_HEADER, TOKEN_PREFIX + refreshToken))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            String cachedRefreshToken = redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + PHONE_NUMBER);
            assertTrue(resultByUpdateToken.contains(cachedRefreshToken));
            assertTrue(resultByUpdateToken.contains("accessToken"));
        }
    }

    @Test
    void givenWrongHeader_whenRefreshToken_thenUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/customer/auth/refresh_token")
                        .header("WRONG_HEADER", TOKEN_PREFIX + refreshToken))
                .andDo(print())
                .andExpect(content().string("Отсутствует заголовок в запросе"));
    }

    @Test
    void givenExpiredRefreshToken_whenRefreshToken_thenUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/customer/auth/refresh_token")
                        .header("Refresh", TOKEN_PREFIX + REFRESH_TOKEN_EXPIRED_OR_REPEATED))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Срок хранения токена обновления истек"));
    }

    @Test
    void givenPhoneNumberAndPassword_whenGenerateToken_thenAccessAndRefreshTokens() throws Exception {
        String resultByUpdateToken = mockMvc.perform(post("/api/v1/customer/auth/generate_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"phoneNumber":"79234251422","password":"Password@123"}
                                """))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String accessToken = read(resultByUpdateToken, "$.accessToken");
        String refreshToken = read(resultByUpdateToken, "$.refreshToken");

        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(PHONE_NUMBER, PASSWORD));
        verify(jwtService, times(1)).generateToken(PHONE_NUMBER, CUSTOMER_ID, CUSTOMER_STATUS, Duration.ofMinutes(2));

        assertTrue(resultByUpdateToken.contains(refreshToken));
        assertTrue(resultByUpdateToken.contains(accessToken));
    }

    @Test
    void givenNotValidPhoneNumberAndPassword_thenUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/customer/auth/generate_token")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"phoneNumber":"79234251427","password":"Password@123"}
                                """))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Аутентификация невозможна - данные о пользователе отсутствуют"));
    }
}
