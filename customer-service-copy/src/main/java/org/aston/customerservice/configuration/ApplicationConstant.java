package org.aston.customerservice.configuration;

public class ApplicationConstant {

    public static final String HEADER_KEY_CUSTOMER_PHONE = "Customer-Phone";

    public static final String HEADER_KEY_CUSTOMER_ID = "Customer-Id";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String REFRESH_HEADER = "Refresh";

    public static final String REDIS_KEY_PREFIX = "refresh:";

    public static final String REGEXP_EMAIL =
            "^(?=.{8,50}$)(?=[^@]{2,30}@)(?!.*[._-]{2})(?!.*[@._-]$)(?!.*[@._-]{2})[a-zA-Z0-9]+(?:[._-][a-zA-Z0-9]+)*@[a-zA-Z0-9-]{2,}\\.[a-z]{2,}$";

    public static final String REGEXP_PHONE_NUMBER = "^7\\d{10}$";

    public static final String REGEXP_DATE = "^(?:19|20)\\d\\d-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12][0-9]|3[01])$";

    public static final String REGEXP_UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    public static final String REGEXP_PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!\"#$%&'()+,-./:;<=>?@\\\\^_`{|}~])[A-Za-z\\d!\"#$%&'()+,-./:;<=>?@\\\\^_`{|}~]{6,20}$";

    public static final String MESSAGE_CUSTOMER_NOT_FOUND = "Клиент не найден!";

    public static final String UNSUPPORTED_CONTENT_TYPE = "Неподдерживаемый тип контента!";

    public static final String MESSAGE_INCORRECT_DATA = "Некорректные данные!";

    public static final String MESSAGE_NO_PARAMETER_AVAILABLE = "Отсутствует параметр!";

    public static final String MESSAGE_INCORRECT_DATA_OR_DATE_FORMAT = "Неверные данные или некорректный формат даты!";

    public static final String MESSAGE_PASSWORD_DOES_NOT_SATISFY_REQUIREMENTS = "Пароль не соответствует требованиям!";

    public static final String MESSAGE_INCORRECT_UUID_FORMAT = "Неверный формат UUID";
}
