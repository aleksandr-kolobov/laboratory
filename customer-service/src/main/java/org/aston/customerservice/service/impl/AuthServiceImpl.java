package org.aston.customerservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.customerservice.dto.request.AuthRequestDto;
import org.aston.customerservice.dto.response.AuthResponseDto;
import org.aston.customerservice.exception.AuthorizationException;
import org.aston.customerservice.exception.CustomerRefreshTokenExpiredException;
import org.aston.customerservice.exception.IllegalArgumentProcessingJwtException;
import org.aston.customerservice.security.CustomUserDetails;
import org.aston.customerservice.security.service.JwtService;
import org.aston.customerservice.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static org.aston.customerservice.configuration.ApplicationConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final StringRedisTemplate redisTemplate;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.jwt-refresh-expiration}")
    private Duration jwtRefreshExpiration;

    @Value("${jwt.jwt-expiration}")
    private Duration jwtExpiration;

    @Override
    public AuthResponseDto authorizeUser(AuthRequestDto authRequestDto) {
        String phoneNumber = authRequestDto.getPhoneNumber();
        String refreshToken;
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getPhoneNumber(), authRequestDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateToken(phoneNumber, userDetails.getUuid(), userDetails.getStatus(), jwtExpiration);
            refreshToken = jwtService.generateToken(phoneNumber, userDetails.getUuid(), userDetails.getStatus(), jwtRefreshExpiration);
            cacheRefreshToken(phoneNumber, refreshToken);
            return new AuthResponseDto(accessToken, refreshToken);
        } catch (AuthenticationException ex) {
            String errMessage = "Аутентификация невозможна - данные о пользователе отсутствуют";
            log.error(errMessage, ex);
            throw new AuthorizationException(errMessage);
        }
    }

    @Override
    public AuthResponseDto refreshJwt(HttpServletRequest request) {
        String refreshToken;
        String phoneNumber;
        String customerId;
        String customerStatus;
        if (!isAuthNotMissing(request)) {
            throw new AuthorizationException("Отсутствует заголовок в запросе");
        } else {
            if (this.isPrefixMissing(request))
                throw new AuthorizationException("Неверный префикс токена");
            else {
                try {
                    refreshToken = getAuthHeader(request);
                    Claims claims = jwtService.validateToken(refreshToken).getPayload();
                    phoneNumber = jwtService.extractPhoneNumber(claims);
                    customerId = jwtService.extractClaim(claims, HEADER_KEY_CUSTOMER_ID);
                    customerStatus = jwtService.extractClaim(claims, HEADER_KEY_CUSTOMER_STATUS);
                    if (isKeyPresent(phoneNumber) && refreshToken.equals(getCachedRefreshToken(phoneNumber))) {
                        removeCachedRefreshToken(phoneNumber);
                        String accessToken = jwtService.generateToken(phoneNumber, customerId, customerStatus, jwtExpiration);
                        String newRefreshToken = jwtService.generateToken(phoneNumber, customerId, customerStatus, jwtRefreshExpiration);
                        cacheRefreshToken(phoneNumber, newRefreshToken);
                        return new AuthResponseDto(accessToken, newRefreshToken);
                    } else {
                        throw new AuthorizationException("Неверный токен доступа");
                    }
                } catch (SignatureException ex) {
                    String errMessage = "Неверная подпись токена обновления";
                    log.error(errMessage, ex);
                    throw new AuthorizationException(errMessage);
                } catch (ExpiredJwtException ex) {
                    log.error(MESSAGE_EXPIRED_REFRESH_JWT + ex.getClaims().getExpiration(), ex);
                    throw new CustomerRefreshTokenExpiredException(ex.getHeader(), ex.getClaims());
                } catch (IllegalArgumentException ex) {
                    String errMessage = "Поставлен некорретный аргумент при парсинге Jwt";
                    log.error(errMessage, ex);
                    throw new IllegalArgumentProcessingJwtException(errMessage);
                }
            }
        }
    }

    private boolean isAuthNotMissing(HttpServletRequest request) {
        var header = request.getHeader(REFRESH_HEADER);
        return header != null;
    }

    private String getAuthHeader(HttpServletRequest request) {
        var header = request.getHeader(REFRESH_HEADER);
        assert header != null;
        return header.replace(TOKEN_PREFIX, "").trim();
    }

    private boolean isPrefixMissing(HttpServletRequest request) {
        var header = request.getHeader(REFRESH_HEADER);
        assert header != null;
        return !header.startsWith(TOKEN_PREFIX);
    }

    private String getCachedRefreshToken(String phoneNumber) {
        return redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + phoneNumber);
    }

    private void removeCachedRefreshToken(String phoneNumber) {
        redisTemplate.delete(REDIS_KEY_PREFIX + phoneNumber);
    }

    private void cacheRefreshToken(String customerPhone, String token) {
        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + customerPhone, token, jwtRefreshExpiration);
    }

    private boolean isKeyPresent(String phoneNumber) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY_PREFIX + phoneNumber));
    }
}
