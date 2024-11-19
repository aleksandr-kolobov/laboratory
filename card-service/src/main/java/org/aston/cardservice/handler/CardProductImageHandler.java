package org.aston.cardservice.handler;

import org.aston.cardservice.dto.request.ImageDto;
import org.aston.cardservice.dto.response.CardProductDto;
import org.aston.cardservice.persistent.enums.CardProductType;
import org.aston.cardservice.service.CardProductService;
import org.aston.cardservice.service.ImageService;

import java.util.List;

/**
 * Промежуточный сервис для обработки изображений.
 * Этот интерфейс предоставляет методы для обработки изобоажений в Minio, используя логику
 *
 * @see CardProductService
 * @see ImageService
 */
public interface CardProductImageHandler {

    /**
     * Загружает изображение в Minio и обновляет название изображения в соответствующей таблице БД.
     *
     * @param imageDto объект типа ImageDto, содержащий id карточного продукта и изображение типа MultipartFormData
     */
    void handleImageUpload(ImageDto imageDto);

    /**
     * Находит карточные продукты и отображает их в объекты Dto.
     *
     * @param cardProductType тип карточного продукта
     * @return объекты типа CardProductDto,содержащие все карточные продукты соответствующего типа
     */
    List<CardProductDto> handleListCardProducts(CardProductType cardProductType);
}
