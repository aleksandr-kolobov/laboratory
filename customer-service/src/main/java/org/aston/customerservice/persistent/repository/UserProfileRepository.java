package org.aston.customerservice.persistent.repository;

import io.micrometer.observation.annotation.Observed;
import org.aston.customerservice.persistent.entity.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью UserProfile.
 */
@Repository
@Observed
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    /**
     * Найти профиль пользователя по идентификатору клиента.
     *
     * @param customerId идентификатор клиента, для которого требуется найти профиль пользователя
     * @return объект типа Optional, содержащий профиль пользователя, если он найден, или пустой, если профиль пользователя не найден
     */
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "customer")
    Optional<UserProfile> findByCustomerCustomerId(UUID customerId);

    /**
     * Определить наличие профиля пользователя по идентификатору клиента.
     *
     * @param customerId идентификатор клиента, для которого требуется определить наличие профиля
     * @return объект типа Boolean, содержащий true, если профиль пользователя найден, или false, если профиль пользователя не найден
     */
    Boolean existsByCustomerCustomerId(UUID customerId);
}