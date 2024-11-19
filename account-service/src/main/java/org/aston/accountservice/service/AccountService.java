package org.aston.accountservice.service;

import org.aston.accountservice.dto.request.ChangeBalanceAccountRequestDto;
import org.aston.accountservice.dto.response.AccountResponseDto;
import org.aston.accountservice.dto.response.GetAccountsListResponseDto;

import java.util.List;
import org.aston.accountservice.dto.request.MasterAccountRequestDto;

/**
 * Сервис для работы со счетами пользователей.
 * Этот интерфейс предоставляет методы для создания счета, изменения баланса счета, получения информации о счете клиента.
 */
public interface AccountService {
    /**
     *  Установка выбранного счета пользователя, как основого.
     *
     * @param masterAccountRequestDto Информация о номере счета пользователя, который необходимо пометить, как основной счет.
     * @param customerId идентификатор пользователя.
     */
    void changeMasterAccountStatus (MasterAccountRequestDto masterAccountRequestDto, String customerId);

    /**
     * Изменение баланса на счете пользователя.
     *
     * @param changeBalanceDto Информация об номере счета пользователя, сумме изменения баланса и действие с балансом счета.
     * @param customerId       id, связанный с клиентом.
     */
    void changeBalance(ChangeBalanceAccountRequestDto changeBalanceDto, String customerId);

    /**
     * Получить информацию о счете по номеру счета и идентификатору клиента.
     *
     * @param accountNumber Номер счета, для которого запрашивается информация.
     * @param customerId    Идентификатор клиента, чей счет проверяется.
     * @return объект типа AccountDto, содержащий информацию о счете, если счет найден и принадлежит указанному клиенту.
     */
    AccountResponseDto getAccountInfo(String accountNumber, String customerId);

    /**
     * Получить список счетов по идентификатору клиента.
     *
     * @param customerId Идентификатор клиента, по которому производится поиск.
     * @return список типа AccountDtoFilter, содержащий информацию о счетах, если счета найдеы и принадлежит указанному клиенту.
     */
    List<GetAccountsListResponseDto> findAccountList(String customerId);
}
