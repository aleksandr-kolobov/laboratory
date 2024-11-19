package org.aston.customerservice.service;

import org.aston.customerservice.dto.response.CustomerIdResponseDto;
import org.aston.customerservice.dto.response.CustomerResponseDto;
import org.aston.customerservice.dto.request.EmailRequestDto;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.response.CustomerFullNameResponseDto;

import java.util.UUID;

/**
 * Сервис для работы с данными клиентов.
 * Этот интерфейс предоставляет методы для поиска клиентов по их идентификаторам и номерам телефонов,
 * изменения email адресов клиентов и добавления новых email адресов.
 */
public interface CustomerService {

    /**
     * Находит клиента по его идентификатору.
     *
     * @param customerId идентификатор клиента
     * @return объект типа CustomerResponseDto с информацией о найденном клиенте
     */
    CustomerResponseDto findById(UUID customerId);

    /**
     * Находит клиента по его номеру телефона.
     *
     * @param phoneNumber номер телефона клиента
     * @return объект типа CustomerResponseDto с информацией о найденном клиенте
     */
    CustomerResponseDto findByPhoneNumber(String phoneNumber);

    /**
     * Проверяет отсутствие личного кабинета у пользователя перед регистрацией.
     * Возвращает идентификатор клиента по его номеру телефона.
     *
     * @param request объект типа PhoneNumberDto с запросом на поиск клиента по номеру телефона
     * @return объект типа CustomerIdDto с идентификатором найденного клиента
     */
    CustomerIdResponseDto checkMissRegistration(PhoneNumberRequestDto request);

    /**
     * Проверяет наличие личного кабинета у пользователя перед авторизацией.
     *
     * @param request объект типа PhoneNumberDto с запросом на поиск клиента по номеру телефона
     * ничего не возвращает
     */
    void checkRegistration(PhoneNumberRequestDto request);

    /**
     * Обновляет адрес электронной почты клиента с использованием предоставленной новой электронной почты и аутентификационного токена.
     *
     * @param newEmailDto Новая информация об адресе электронной почты для обновления.
     * @param phoneNumber Номер телефона клиента.
     */
    void updateEmail(EmailRequestDto newEmailDto, String phoneNumber);

    /**
     * Добавляет новый email адрес клиенту.
     *
     * @param emailDto    объект типа EmailDto с новым email адресом
     * @param phoneNumber номер телефона клиента, для которого нужно добавить email адрес
     */
    void addEmail(EmailRequestDto emailDto, String phoneNumber);

    /**
     * Метод позволяет получить ФИО customer по id
     *
     * @param customerId - идентификатор customer
     * @return ФИО клиента
     */
    CustomerFullNameResponseDto getFullName(String customerId);
}