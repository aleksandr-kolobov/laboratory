package org.aston.customerservice.service;

import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.response.StatusResponseDto;
import org.aston.customerservice.dto.response.StatusTypeResponseDto;

/**
 * Сервис для получения статуса клиента.
 * Этот интерфейс предоставляет метод для получения статуса клиента по его номеру телефона.
 */
public interface StatusService {

    /**
     * Получает статус клиента по его номеру телефона.
     *
     * @param phoneNumber объект типа PhoneNumberRequestDto с номером телефона клиента
     * @return объект типа StatusResponseDto с информацией о статусе клиента
     */
    StatusResponseDto getCustomerStatus(PhoneNumberRequestDto phoneNumber);

    /**
     * Получает тип статуса клиента по его идентификатору.
     *
     * @param customerId объект типа String с id клиента.
     * @return объект типа StatusTypeResponseDto с информацией о типе статуса клиента.
     */
    StatusTypeResponseDto getStatusType(String customerId);
}