package org.aston.cardservice.configuration;

import java.util.Arrays;
import java.util.List;

public class ApplicationConstant {

    public static final String FILE_EXTENSION_DELIMITER = ".";

    public static final int IMG_EXPIRY = 60 * 60 * 24;

    public static final String MINIO_SEC_FAILURE_MSG = "Ошибка алгоритма шифрования";

    public static final String MINIO_IMG_SAVE_FAILURE_MSG = "Ошибка при сохранениии изображения";

    public static final String MINIO_IMG_SAVE_SUCCESS_MSG = "Ошибка при загрузке изображения";

    public static final String MINIO_BUCKET_CREATE_FAILURE_MSG = "Ошибка при создании бакета";

    public static final String MINIO_BUCKET_EXISTS_FAILURE_MSG = "Ошибка при проверки существования бакета";

    public static final String MINIO_GET_IMAGE_URL_FAILURE_MSG = "Ошибка при получении адреса изображения";

    public static final String WRONG_CARD_PRODUCT_TYPE_MSG = "Неверное название карточного продукта";

    public static final String MULTIPART_FORM_DATA_FAILURE_DEFAULT_MSG =  "Ошибка валидации файла типа MultipartFormData";

    public static final String FILE_NOT_IMAGE_MSG = "Файл должен быть изображением";

    public static final String FILE_EMPTY_MSG = "Файл не должен быть пустым";

    public static final String FILE_NAME_EMPTY_MESSAGE = "Отсутствует название файла";

    public static final String REG_EX_CARD_PRODUCT_TYPE = "^(DEBIT|DEPOSIT|CREDIT)$";

    public static final List<String> SUPPORTED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp");

}
