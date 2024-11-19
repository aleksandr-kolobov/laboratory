package org.aston.customerservice.persistent.repository;

import io.micrometer.observation.annotation.Observed;
import org.aston.customerservice.persistent.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Customer.
 */
@Repository
@Observed
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    /**
     * Найти клиента по номеру телефона.
     *
     * @param phoneNumber Номер телефона клиента.
     * @return Найденный клиент, обернутый в Optional, или пустой Optional, если клиент не найден.
     */
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "userProfile")
    Optional<Customer> findByPhoneNumber(String phoneNumber);

    /**
     * Проверить существование клиента с указанной электронной почтой.
     *
     * @param email Электронная почта для проверки существования у клиента.
     * @return true, если существует клиент с указанной электронной почтой, в противном случае - false.
     */
    boolean existsByEmail(String email);

}
