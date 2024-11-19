package org.aston.cardservice.persistent.repository;

import io.micrometer.observation.annotation.Observed;
import org.aston.cardservice.persistent.entity.CardProduct;
import org.aston.cardservice.persistent.enums.CardProductType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью CardProduct.
 */
@Repository
@Observed
public interface CardProductRepository extends JpaRepository<CardProduct, String> {

    /**
     * Находит все карточные продукты.
     *
     * @return Найденные карточные продукты.
     */
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"card"})
    List<CardProduct> findByTypeCard(CardProductType cardProductType);
}
