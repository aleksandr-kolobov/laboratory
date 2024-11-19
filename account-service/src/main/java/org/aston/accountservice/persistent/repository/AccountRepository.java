package org.aston.accountservice.persistent.repository;

import io.micrometer.observation.annotation.Observed;
import org.aston.accountservice.persistent.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы со счетами пользователей - сущность Account.
 */
@Repository
@Observed
public interface AccountRepository extends JpaRepository<Account, UUID> {

    /**
     * Найти информацию о счете по номеру счета.
     *
     * @param accountNumber Номер счета, по которому осуществляется поиск учетной информации.
     * @return объект типа Optional, содержащий информацию о счете, если информация о счете найдена, иначе пустой Optional, если счеи не найден.
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Найти информацию о всех счетах пользователя с определенным id.
     *
     * @param customerId id клиента, по которому осуществляется поиск его счетов.
     * @return объект типа List, содержащий объекты типа Account, хранящие информацию о счетах пользователя.
     */
    List<Account> findAllByCustomerId(UUID customerId);
}
