package org.aston.cardservice.service;

import org.aston.cardservice.dto.request.ImageDto;

/**
 * Сервис для работы с изображениями в Minio.
 * Этот интерфейс предоставляет методы для загрузки изобоажений в Minio, получения url изображений
 */
public interface ImageService {

    /**
     * Загружает изображение в Minio.
     *
     * @param image изображение
     * @return url  изображения из Minio
     */
    String upload(ImageDto image);

    /**
     * Получает url  изображения из Minio по имени изображения.
     *
     * @param imageName имя изображения
     * @return url  изображения из Minio
     */
    public String getImageUrl(String imageName);
}
