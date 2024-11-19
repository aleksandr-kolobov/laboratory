package org.aston.cardservice.data;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.aston.cardservice.dto.request.ImageDto;
import org.aston.cardservice.dto.response.CardProductDto;
import org.aston.cardservice.persistent.entity.CardProduct;
import org.aston.cardservice.persistent.enums.CardProductLevel;
import org.aston.cardservice.persistent.enums.CardProductType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.StreamSupport;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.aston.cardservice.configuration.ApplicationConstant.IMG_EXPIRY;

public class CardProductData {

    public static String INVALID = "INVALID";

    public static String MULTIPART_FILE_PARAM = "file";

    public static String INVALID_ID = "INVALID";

    public static String IMAGE_URL_FAILURE = "Ошибка при получении адреса изображения";

    public static String CARD_PRODUCT_ID_PARAM = "cardProductId";

    public static String CARD_PRODUCT_TYPE_PARAM = "card_product_type";

    public static String CARD_PRODUCT_TYPE_VALUE = "DEBIT";

    public static String CARD_PRODUCT_ID_VALUE = "CP1";

    public static String BUCKET = "images";

    public static String IMAGE_NAME = "image.png";

    public static final String IMAGE_URL = "http://localhost:55983/images/8d0f5e10-69b0-4ebd-9052-4bad5ba17242.png";

    public static ClassPathResource classPathResource = new ClassPathResource("/static/" + IMAGE_NAME);

    public static CardProduct createCardProduct() {
        return CardProduct.builder()
                .cardProductId("CP001")
                .image(IMAGE_NAME)
                .nameProduct("test_product")
                .feeUse(BigDecimal.TEN)
                .paymentSystem("Visa")
                .isVirtual(false)
                .typeCard(CardProductType.DEBIT)
                .level(CardProductLevel.GOLD)
                .build();
    }

    public static MockMultipartFile createMockMultipartFile() throws IOException {
        return new MockMultipartFile(
                MULTIPART_FILE_PARAM,
                IMAGE_NAME,
                MediaType.MULTIPART_FORM_DATA_VALUE,
                classPathResource.getInputStream());
    }

    public static MockMultipartFile createMockMultipartFileWithEmptyName() throws IOException {
        return new MockMultipartFile(
                MULTIPART_FILE_PARAM,
                EMPTY,
                MediaType.MULTIPART_FORM_DATA_VALUE,
                classPathResource.getInputStream());
    }

    public static MockMultipartFile createMockMultipartFileWithWrongExtension() throws IOException {
        return new MockMultipartFile(
                MULTIPART_FILE_PARAM,
                "text.txt",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                classPathResource.getInputStream());
    }

    public static long countObjectsFromMinio(Iterable<Result<Item>> objects) {

        return StreamSupport.stream(objects.spliterator(), false).count();
    }

    public static CardProductDto createCardProductDto() {
        return new CardProductDto("test_product", IMAGE_URL);
    }

    public static MockMultipartFile createEmptyMockMultipartFile() {
        return new MockMultipartFile(MULTIPART_FILE_PARAM, IMAGE_NAME, "image/png", new byte[0]);
    }

    public static ImageDto createImageDto() {
        return new ImageDto(CARD_PRODUCT_ID_VALUE, createEmptyMockMultipartFile());
    }

    public static GetPresignedObjectUrlArgs createGetPresignedObjectUrlArgs() {
        return GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(BUCKET).object(IMAGE_NAME).expiry(IMG_EXPIRY).build();
    }

    public static BucketExistsArgs createBucketExistsArgs() {
        return BucketExistsArgs.builder().bucket(BUCKET).build();
    }

    public static MakeBucketArgs createMakeBucketArgs() {
        return MakeBucketArgs.builder().bucket(BUCKET).build();
    }
}
