package org.aston.customerservice.service;

import org.aston.customerservice.dto.request.PasswordRecoveryRequestDto;
import org.aston.customerservice.dto.request.ChangePasswordRequestDto;
import org.aston.customerservice.dto.request.UserProfileCreationRequestDto;

/**
 * Сервис для работы с профилями пользователей.
 * Этот интерфейс предоставляет методы для создания, изменения учетных записей пользователей и восстановления паролей.
 */
public interface UserProfileService {

    /**
     * Создает учетную запись пользователя на основе предоставленных данных.
     *
     * @param request объект типа PersonalAccountCreationRequestDto с данными для создания учетной записи
     */
    void createUserProfile(UserProfileCreationRequestDto request);

    /**
     * Восстанавливает пароль пользователя на основе предоставленной информации.
     *
     * @param requestDto объект типа PasswordRecoveryRequestDto с данными для восстановления пароля
     */
    void recoveryPassword(PasswordRecoveryRequestDto requestDto);

    /**
     * Смена старого пароля клиента с использованием предоставленного нового пароля и аутентификационного токена.
     *
     * @param dto   Информация об новом пароле для замены старого.
     * @param token Аутентификационный токен, связанный с клиентом.
     */
    void changePassword(ChangePasswordRequestDto dto, String token);
}