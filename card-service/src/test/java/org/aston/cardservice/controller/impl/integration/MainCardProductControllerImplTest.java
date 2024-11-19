package org.aston.cardservice.controller.impl.integration;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.aston.cardservice.persistent.enums.CardProductType;
import org.aston.cardservice.persistent.repository.CardProductRepository;
import org.aston.cardservice.service.CardProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import static org.aston.cardservice.configuration.ApplicationConstant.*;
import static org.aston.cardservice.data.CardProductData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MainCardProductControllerImplTest extends AbstractCardProductControllerImplTest {
/*
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private CardProductService cardProductService;
    @SpyBean
    private CardProductRepository cardProductRepository;

    private MinioClient client;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.url}")
    private String url;

    private MockMultipartFile mockMultipartFile;

    private MockMultipartFile mockEmptyMultipartFile;

    private MockMultipartFile mockMultipartFileWithEmptyName;

    private MockMultipartFile mockMultipartFileWithWrongExtension;

    @BeforeAll
    public void init() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, KeyManagementException {
        client = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        initMockMultipartFiles();
        client.ignoreCertCheck();
        client.makeBucket(MakeBucketArgs.builder()
                                  .bucket(bucket)
                                  .build());

        try (InputStream inputStream = mockMultipartFile
                .getInputStream()) {
            client.putObject(PutObjectArgs
                                     .builder()
                                     .bucket(bucket)
                                     .object(IMAGE_NAME)
                                     .stream(inputStream, inputStream.available(), -1)
                                     .build());
        }
    }

    private void initMockMultipartFiles() throws IOException {
        mockMultipartFile = createMockMultipartFile();
        mockEmptyMultipartFile = createEmptyMockMultipartFile();
        mockMultipartFileWithEmptyName = createMockMultipartFileWithEmptyName();
        mockMultipartFileWithWrongExtension = createMockMultipartFileWithWrongExtension();
    }

    @Test
    void givenCardProductType_whenListAllCardsWithImages_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/card-service/card-product/list_card_products")
                                .param("card_product_type", "DEBIT"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> result
                        .getResponse()
                        .getContentAsString().contains("images/" + IMAGE_NAME));

        verify(cardProductService, times(1)).getAllCardProducts(CardProductType.DEBIT);
        verify(cardProductRepository, times(1)).findByTypeCard(CardProductType.DEBIT);
    }

    @Test
    void givenWrongCardProductType_whenListAllCardsWithImages_thenBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/card-service/card-product/list_card_products")
                                .param("card_product_type", INVALID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> result
                        .getResponse()
                        .getContentAsString().contains(WRONG_CARD_PRODUCT_TYPE_MSG));

        verifyNoInteractions(cardProductService);
        verifyNoInteractions(cardProductRepository);
    }

    @Test
    void givenMultiPartFile_whenUpLoadImage_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                                .multipart("/api/v1/card-service/card-product/upload_image")
                                .file(mockMultipartFile)
                                .param(CARD_PRODUCT_ID_PARAM, CARD_PRODUCT_ID_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        Iterable<Result<Item>> objects = getResultIterable();
        String imageUrl = getPresignedObjectUrl();

        verify(cardProductService, times(1)).updateCardProductImage(any(), anyString());
        verify(cardProductRepository, times(1)).save(any());
        verify(cardProductRepository, times(1)).findById(CARD_PRODUCT_ID_VALUE);
        assertEquals(2, countObjectsFromMinio(objects));
        assertTrue(imageUrl.contains("images/" + IMAGE_NAME));
    }

    @Test
    void givenWrongCardProductId_whenUpLoadImage_thenNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                                .multipart("/api/v1/card-service/card-product/upload_image")
                                .file(mockMultipartFile)
                                .param(CARD_PRODUCT_ID_PARAM, INVALID_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> result
                        .getResponse()
                        .getContentAsString().equals("Карточный продукт не найден"));

        verify(cardProductService, times(1)).updateCardProductImage(any(), anyString());
        verify(cardProductRepository, times(0)).save(any());
        verify(cardProductRepository, times(1)).findById(INVALID_ID);
    }

    @ParameterizedTest
    @MethodSource("mockMultipartFiles")
    void givenWrongInput_whenUpLoadImage_thenExpectBadRequest(MockMultipartFile file, String errorMessage) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                                .multipart("/api/v1/card-service/card-product/upload_image")
                                .file(file)
                                .param(CARD_PRODUCT_ID_PARAM, CARD_PRODUCT_ID_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> result
                        .getResponse()
                        .getContentAsString().contains(errorMessage));
    }

    private Stream<Object[]> mockMultipartFiles() {
        return Stream.of(
                new Object[]{mockEmptyMultipartFile, FILE_EMPTY_MSG},
                new Object[]{mockMultipartFileWithEmptyName, FILE_NAME_EMPTY_MESSAGE},
                new Object[]{mockMultipartFileWithWrongExtension, FILE_NOT_IMAGE_MSG}
        );
    }

    private Iterable<Result<Item>> getResultIterable() {
        return client.listObjects(
                ListObjectsArgs
                        .builder()
                        .bucket(bucket)
                        .build()
        );
    }

    private String getPresignedObjectUrl() throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException, XmlParserException, ServerException {
        return client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs
                        .builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(IMAGE_NAME)
                        .expiry(IMG_EXPIRY)
                        .build()
        );
    }
*/
}
