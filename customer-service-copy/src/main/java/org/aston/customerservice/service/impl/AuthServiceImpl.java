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
import org.aston.customerservice.security.CustomUserDetails;
import org.aston.customerservice.security.service.JwtService;
import org.aston.customerservice.service.AuthService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
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
            String accessToken = jwtService.generateToken(phoneNumber, userDetails.getUuid());
            refreshToken = jwtService.generateRefreshToken(phoneNumber, userDetails.getUuid());
            cacheRefreshToken(phoneNumber, refreshToken);
            return new AuthResponseDto(accessToken, refreshToken);
        } catch (AuthenticationException ex) {
            String errMessage = "Аутентификация невозможна - данные о пользователе отсутствуют";
            log.error(errMessage, ex);
            throw new AuthorizationException(HttpStatus.UNAUTHORIZED, errMessage);
        }
    }

    @Override
    public AuthResponseDto refreshJwt(HttpServletRequest request) {
        String refreshToken;
        String phoneNumber;
        String customerId;
        if (!isAuthNotMissing(request)) {
            throw new AuthorizationException(HttpStatus.UNAUTHORIZED, "Отсутствует заголовок в запросе");
        } else {
            if (this.isPrefixMissing(request))
                throw new AuthorizationException(HttpStatus.UNAUTHORIZED, "Неверный префикс токена");
            else {
                try {
                    refreshToken = getAuthHeader(request);
                    Claims claims = jwtService.validateToken(refreshToken).getPayload();
                    phoneNumber = jwtService.extractPhoneNumber(claims);
                    customerId = jwtService.extractCustomerId(claims);
                    if (isKeyPresent(phoneNumber) && refreshToken.equals(getCachedRefreshToken(phoneNumber))) {
                        removeCachedRefreshToken(phoneNumber);
                        String accessToken = jwtService.generateToken(phoneNumber, customerId);
                        String newRefreshToken = jwtService.generateRefreshToken(phoneNumber, customerId);
                        cacheRefreshToken(phoneNumber, newRefreshToken);
                        return new AuthResponseDto(accessToken, newRefreshToken);
                    } else {
                        throw new AuthorizationException(HttpStatus.UNAUTHORIZED, "Неверный токен доступа");
                    }
                } catch (SignatureException ex) {
                    String errMessage = "Неверная подпись токена обновления";
                    log.error(errMessage, ex);
                    throw new AuthorizationException(HttpStatus.UNAUTHORIZED, errMessage);
                } catch (ExpiredJwtException ex) {
                    log.error("Срок хранения токена обновления истек: " + ex.getClaims().getExpiration(), ex);
                    throw new CustomerRefreshTokenExpiredException(ex.getHeader(), ex.getClaims());
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

    private void cacheRefreshToken(String phoneNumber, String token) {
        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + phoneNumber, token, Duration.ofMinutes(30));
    }

    private boolean isKeyPresent(String phoneNumber) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY_PREFIX + phoneNumber));
    }
}
