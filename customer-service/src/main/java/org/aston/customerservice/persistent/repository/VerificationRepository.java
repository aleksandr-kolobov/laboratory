package org.aston.customerservice.persistent.repository;

import io.micrometer.observation.annotation.Observed;
import org.aston.customerservice.persistent.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Verification.
 */
@Repository
@Observed
public interface VerificationRepository extends JpaRepository<Verification, UUID> {

    /**
     * Найти верификацию по идентификатору профиля пользователя.
     *
     * @param customerId идентификатор пользователя, для которого требуется найти верификацию
     * @return объект типа Optional, содержащий верификацию, если она найдена, или пустой, если верификация не найдена
     */
    Optional<Verification> findByCustomerId(UUID customerId);
}