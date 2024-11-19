package org.aston.cardservice.service;

import org.aston.cardservice.dto.request.ImageDto;
import org.aston.cardservice.persistent.entity.CardProduct;
import org.aston.cardservice.persistent.enums.CardProductType;

import java.util.List;

/**
 * Сервис для работы с карточными продуктами.
 * Этот интерфейс предоставляет методы для поиска и изменения данных карточных продуктов по их типу
 */
public interface CardProductService {

    /**
     * Находит карточные продукты.
     *
     * @param cardProductType тип карточного продукта
     * @return объекты типа CardProduct,содержащие все карточные продукты соответствующего типа
     */

    List<CardProduct> getAllCardProducts(CardProductType cardProductType);

    /**
     * Сохраняет название изображения у CardProduct.
     *
     * @param imageDto  объект типа ImageDto,содержащий  изображение и индентификатор CardProduct
     * @param imageName строка, содержащя название изображения
     */

    void updateCardProductImage(ImageDto imageDto, String imageName);
}
