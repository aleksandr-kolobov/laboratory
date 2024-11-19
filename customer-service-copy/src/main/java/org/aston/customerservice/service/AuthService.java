package org.aston.customerservice.service;


import jakarta.servlet.http.HttpServletRequest;
import org.aston.customerservice.dto.request.AuthRequestDto;
import org.aston.customerservice.dto.response.AuthResponseDto;


/**
 * Сервис аутентификации пользователей.
 * Этот интерфейс предоставляет методы для аутентификации пользователей и обновления токенов доступа.
 */
public interface AuthService {

    /**
     * Аутентифицирует пользователя на основе предоставленных учетных данных.
     *
     * @param authRequestDto объект типа AuthRequest, содержащий учетные данные пользователя для аутентификации
     * @return объект типа AuthResponse, содержащий результат аутентификации, включая токен доступа и информацию о пользователе
     */
    AuthResponseDto authorizeUser(AuthRequestDto authRequestDto);

    /**
     * Обновляет токен доступа пользователя на основе полученного запроса.
     *
     * @param request объект типа HttpServletRequest, содержащий запрос на обновление токена доступа
     * @return объект типа AuthResponse, содержащий обновленный токен доступа и информацию о пользователе
     */
    AuthResponseDto refreshJwt(HttpServletRequest request);
}
