package org.aston.customerservice.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.aston.customerservice.dto.request.AuthRequestDto;
import org.aston.customerservice.dto.response.AuthResponseDto;
import org.aston.customerservice.exception.AuthorizationException;
import org.aston.customerservice.exception.CustomerRefreshTokenExpiredException;
import org.aston.customerservice.exception.IllegalArgumentProcessingJwtException;
import org.aston.customerservice.security.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.aston.customerservice.configuration.ApplicationConstant.HEADER_KEY_CUSTOMER_ID;
import static org.aston.customerservice.configuration.ApplicationConstant.HEADER_KEY_CUSTOMER_STATUS;
import static org.aston.customerservice.data.AuthTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Тест для AuthService")
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    JwtService jwtService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    Authentication authentication;
    @Mock
    StringRedisTemplate redisTemplate;
    @Mock
    ValueOperations<String, String> valueOperations;
    @Mock
    Jws mockJws;

    private Duration jwtExpiration;

    private Duration jwtRefreshExpiration;

    private AuthRequestDto authRequestDto;

    private MockHttpServletRequest request_mock;

    @BeforeEach
    public void init() {
        authRequestDto = new AuthRequestDto(PHONE_NUMBER, PASSWORD);
        request_mock = new MockHttpServletRequest();
        jwtExpiration = Duration.ofMinutes(2);
        jwtRefreshExpiration = Duration.ofMinutes(30);
        ReflectionTestUtils.setField(authService, "jwtExpiration", Duration.ofMinutes(2));
        ReflectionTestUtils.setField(authService, "jwtRefreshExpiration", Duration.ofMinutes(30));
    }

    @Test
    void givenUserIsAuthenticated_thenAccessAndRefreshTokens() {

        authentication.setAuthenticated(true);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getPhoneNumber(),
                authRequestDto.getPassword()))).thenReturn(authentication);
        when(jwtService.generateToken(PHONE_NUMBER, CUSTOMER_ID, CUSTOMER_STATUS, jwtExpiration)).thenReturn(ACCESS_TOKEN);
        when(jwtService.generateToken(PHONE_NUMBER, CUSTOMER_ID, CUSTOMER_STATUS, jwtRefreshExpiration)).thenReturn(REFRESH_TOKEN);
        when(authentication.getPrincipal()).thenReturn(USER_DETAILS);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        valueOperations.set(REDIS_KEY_PREFIX + PHONE_NUMBER, REFRESH_TOKEN, Duration.ofMinutes(30));

        AuthResponseDto authResponse = authService.authorizeUser(authRequestDto);

        verify(jwtService, times(1)).generateToken(PHONE_NUMBER, CUSTOMER_ID, CUSTOMER_STATUS, jwtExpiration);
        verify(jwtService, times(1)).generateToken(PHONE_NUMBER, CUSTOMER_ID, CUSTOMER_STATUS, jwtRefreshExpiration);
        assertTrue(authResponse.getAccessToken().equals(ACCESS_TOKEN));
        assertTrue(authResponse.getRefreshToken().equals(REFRESH_TOKEN));
    }

    @Test
    void givenUserIsNotAuthenticated_whenAuthorize_thenThrowCustomerAuthenticationException() {

        authentication.setAuthenticated(false);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getPhoneNumber(),
                authRequestDto.getPassword()))).thenThrow(mock(AuthenticationException.class));

        assertThrows(AuthorizationException.class, () -> authService.authorizeUser(authRequestDto));
    }

    @Test
    void givenRefreshToken_whenRefreshJwt_thenRefreshAndAccessTokens() {

        Claims customClaims = Jwts.claims()
                .add("customerId", CUSTOMER_ID)
                .add("customerStatus", CUSTOMER_STATUS)
                .subject(PHONE_NUMBER)
                .build();
        when(jwtService.validateToken(REFRESH_TOKEN)).thenReturn(mockJws);
        when(jwtService.validateToken(REFRESH_TOKEN).getPayload()).thenReturn(customClaims);
        request_mock.addHeader(REFRESH_HEADER, TOKEN_PREFIX + REFRESH_TOKEN);
        when(jwtService.extractPhoneNumber(customClaims)).thenReturn(PHONE_NUMBER);
        when(jwtService.extractClaim(customClaims, HEADER_KEY_CUSTOMER_ID)).thenReturn(CUSTOMER_ID);
        when(jwtService.extractClaim(customClaims, HEADER_KEY_CUSTOMER_STATUS)).thenReturn(CUSTOMER_STATUS);
        valueOperations.set(REDIS_KEY_PREFIX + PHONE_NUMBER, REFRESH_TOKEN, Duration.ofMinutes(30));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.hasKey(REDIS_KEY_PREFIX + PHONE_NUMBER)).thenReturn(true);
        when(valueOperations.get(REDIS_KEY_PREFIX + PHONE_NUMBER)).thenReturn(REFRESH_TOKEN);
        when(redisTemplate.delete(REDIS_KEY_PREFIX + PHONE_NUMBER)).thenReturn(true);
        when(jwtService.generateToken(PHONE_NUMBER, CUSTOMER_ID, CUSTOMER_STATUS, jwtExpiration)).thenReturn(ACCESS_TOKEN);
        when(jwtService.generateToken(PHONE_NUMBER, CUSTOMER_ID, CUSTOMER_STATUS, jwtRefreshExpiration)).thenReturn(NEW_REFRESH_TOKEN);
        valueOperations.set(REDIS_KEY_PREFIX + PHONE_NUMBER, NEW_REFRESH_TOKEN, Duration.ofMinutes(30));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        AuthResponseDto authResponse = authService.refreshJwt(request_mock);

        verify(jwtService, times(2)).validateToken(REFRESH_TOKEN);
        verify(valueOperations, times(2)).set(REDIS_KEY_PREFIX + PHONE_NUMBER, NEW_REFRESH_TOKEN, Duration.ofMinutes(30));
        assertTrue(authResponse.getRefreshToken().contains(NEW_REFRESH_TOKEN));
        assertTrue(authResponse.getAccessToken().contains(ACCESS_TOKEN));
    }

    @Test
    void givenRefreshJwt_whenRefreshToken_thenThrowRefreshHeaderNotFoundException() {

        request_mock.addHeader(WRONG_HEADER, TOKEN_PREFIX + REFRESH_TOKEN);

        assertThrows(AuthorizationException.class, () -> authService.refreshJwt(request_mock));
    }

    @Test
    void givenRefreshJwt_whenRefreshToken_thenThrowJwtException() {

        request_mock.addHeader(REFRESH_HEADER, TOKEN_PREFIX + REFRESH_TOKEN);
        when(jwtService.validateToken(REFRESH_TOKEN)).thenThrow(JwtException.class);

        assertThrows(JwtException.class, () -> authService.refreshJwt(request_mock));
    }

    @Test
    void givenRefreshJwt_whenRefreshToken_thenThrowPrefixMissingException() {

        request_mock.addHeader(REFRESH_HEADER, WRONG_TOKEN_PREFIX + REFRESH_TOKEN);

        assertThrows(AuthorizationException.class, () -> authService.refreshJwt(request_mock));
    }

    @Test
    void givenRefreshJwt_whenRefreshToken_thenThrowWrongRefreshTokenException() {

        request_mock.addHeader(REFRESH_HEADER, TOKEN_PREFIX + REFRESH_TOKEN);
        Claims customClaims = Jwts.claims().add("customerId", CUSTOMER_ID).build();
        when(jwtService.validateToken(REFRESH_TOKEN)).thenReturn(mockJws);
        when(jwtService.validateToken(REFRESH_TOKEN).getPayload()).thenReturn(customClaims);
        request_mock.addHeader(REFRESH_HEADER, TOKEN_PREFIX + REFRESH_TOKEN);
        when(jwtService.extractPhoneNumber(customClaims)).thenReturn(PHONE_NUMBER);
        valueOperations.set(REDIS_KEY_PREFIX + PHONE_NUMBER, REFRESH_TOKEN, Duration.ofMinutes(30));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.hasKey(REDIS_KEY_PREFIX + PHONE_NUMBER)).thenReturn(true);
        when(valueOperations.get(REDIS_KEY_PREFIX + PHONE_NUMBER)).thenReturn(REFRESH_TOKEN_EXPIRED_OR_REPEATED);

        assertThrows(AuthorizationException.class, () -> authService.refreshJwt(request_mock));
    }

    @Test
    void givenRefreshJwt_whenRefreshToken_thenThrowCustomerTokenSignatureException() {

        request_mock.addHeader(REFRESH_HEADER, TOKEN_PREFIX + REFRESH_TOKEN_WRONG_SIGNATURE);

        when(jwtService.validateToken(REFRESH_TOKEN_WRONG_SIGNATURE)).thenThrow(SignatureException.class);

        assertThrows(AuthorizationException.class, () -> authService.refreshJwt(request_mock));
    }

    @Test
    void givenRefreshJwt_whenRefreshToken_thenThrowIllegalArgumentProcessingJwtException() {

        request_mock.addHeader(REFRESH_HEADER, TOKEN_PREFIX + REFRESH_TOKEN_WRONG_SIGNATURE);

        doAnswer((invocation) -> {
            throw new IllegalArgumentException();
        }).when(jwtService).validateToken(REFRESH_TOKEN_WRONG_SIGNATURE);

        assertThrows(IllegalArgumentProcessingJwtException.class, () -> authService.refreshJwt(request_mock));
    }

    @Test
    void givenRefreshJwt_whenRefreshToken_thenThrowCustomerRefreshTokenExpiredException() {

        request_mock.addHeader(REFRESH_HEADER, TOKEN_PREFIX + REFRESH_TOKEN_EXPIRED_OR_REPEATED);
        ExpiredJwtException expiredJwtException = mock(ExpiredJwtException.class);
        when(jwtService.validateToken(REFRESH_TOKEN_EXPIRED_OR_REPEATED)).thenThrow(expiredJwtException);
        Claims claims = mock(Claims.class);
        when(claims.getExpiration()).thenReturn(Date.from(Instant.now()));
        when(expiredJwtException.getClaims()).thenReturn(claims);

        assertThrows(CustomerRefreshTokenExpiredException.class, () -> authService.refreshJwt(request_mock));
    }
}
