package org.aston.cardservice.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.aston.cardservice.dto.request.ImageDto;
import org.aston.cardservice.exception.MinioSecurityException;
import org.aston.cardservice.exception.ProcessingImageFailureException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import static org.aston.cardservice.configuration.ApplicationConstant.*;
import static org.aston.cardservice.data.CardProductData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @InjectMocks
    private ImageServiceImpl imageService;
    @Mock
    private MinioClient minioClient;
    @Spy
    private static MockMultipartFile mockMultipartFile;

    private static ImageDto imageDto;

    private static GetPresignedObjectUrlArgs getPresignedObjectUrlArgs;

    private static BucketExistsArgs bucketExistsArgs;

    private static MakeBucketArgs makeBucketArgs;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(imageService, "bucket", BUCKET);
    }

    @BeforeAll
    public static void initBeforeAll() throws IOException {
        imageDto = createImageDto();
        getPresignedObjectUrlArgs = createGetPresignedObjectUrlArgs();
        bucketExistsArgs = createBucketExistsArgs();
        makeBucketArgs = createMakeBucketArgs();
        mockMultipartFile = createMockMultipartFile();
    }

    @Test
    void givenImageDto_whenUploadImage_thenReturnImageName() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        when(minioClient.bucketExists(createBucketExistsArgs())).thenReturn(false);

        String imageNameResult = imageService.upload(createImageDto());

        assertNotNull(imageNameResult);
        verify(minioClient, times(1)).makeBucket(makeBucketArgs);
    }

    @Test
    void givenImageName_whenGetImageUrl_thenImageUrl() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        when(minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs)).thenReturn(IMAGE_URL);

        String imageUrlResult = imageService.getImageUrl(IMAGE_NAME);

        assertEquals(IMAGE_URL, imageUrlResult);
        verify(minioClient, times(1)).getPresignedObjectUrl(getPresignedObjectUrlArgs);
    }

    @ParameterizedTest
    @MethodSource("minioSecurityExceptions")
    void givenWrongImageDto_whenUploadImage_thenThrowMinioSecurityException(Throwable exception) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        doAnswer((invocation) -> {
            throw exception;
        }).when(minioClient).putObject(any());

        MinioSecurityException thrownException = assertThrows(MinioSecurityException.class,
                                                              () -> imageService.upload(imageDto));

        assertEquals(MINIO_SEC_FAILURE_MSG, thrownException.getMessage());
    }

    @ParameterizedTest
    @MethodSource("minioAndIOExceptions")
    void givenWrongImageDto_whenGetImageUrl_thenThrowMinioException(Exception exception) throws Exception {
        doAnswer((invocation) -> {
            throw exception;
        }).when(minioClient).getPresignedObjectUrl(getPresignedObjectUrlArgs);

        ProcessingImageFailureException thrownException = assertThrows(ProcessingImageFailureException.class,
                                                                       () -> imageService.getImageUrl(IMAGE_NAME));

        assertEquals(MINIO_GET_IMAGE_URL_FAILURE_MSG, thrownException.getMessage());
    }

    @ParameterizedTest
    @MethodSource("minioAndIOExceptions")
    void givenWrongImageDto_whenUploadImage_thenThrowProcessingImageFailureException(Exception exception) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        doAnswer((invocation) -> {
            throw exception;
        }).when(minioClient).putObject(any());

        ProcessingImageFailureException thrownException = assertThrows(ProcessingImageFailureException.class,
                                                                       () -> imageService.upload(createImageDto()));
        assertEquals(MINIO_IMG_SAVE_FAILURE_MSG, thrownException.getMessage());
    }

    @Test
    void givenFileInputStream_whenUploadImage_thenThrowIOException() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        when(mockMultipartFile.getInputStream()).thenThrow(IOException.class);

        ProcessingImageFailureException thrownException = assertThrows(ProcessingImageFailureException.class,
                                                                       () -> imageService.upload(new ImageDto(CARD_PRODUCT_ID_VALUE, mockMultipartFile)));

        assertEquals(MINIO_IMG_SAVE_SUCCESS_MSG, thrownException.getMessage());
        verify(minioClient, times(0)).putObject(any());
    }


    @ParameterizedTest
    @MethodSource("minioSecurityExceptions")
    void givenWrongImageDto_whenCreateBucket_thenThrowMinioSecurityException(Throwable exception) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
        when(minioClient.bucketExists(bucketExistsArgs)).thenThrow(exception);

        MinioSecurityException thrownException = assertThrows(MinioSecurityException.class, () -> imageService.upload(imageDto));

        assertEquals(MINIO_SEC_FAILURE_MSG, thrownException.getMessage());
    }

    @ParameterizedTest
    @MethodSource("minioAndIOExceptions")
    void givenWrongImageDto_whenCreateBucket_thenThrowMinioAndIOException(Throwable exception) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
        doAnswer((invocation) -> {
            throw exception;
        }).when(minioClient).bucketExists(bucketExistsArgs);

        ProcessingImageFailureException thrownException = assertThrows(ProcessingImageFailureException.class,
                                                                       () -> imageService.upload(imageDto));

        assertEquals(MINIO_BUCKET_EXISTS_FAILURE_MSG, thrownException.getMessage());
    }

    @ParameterizedTest
    @MethodSource("minioSecurityExceptions")
    void givenWrongImageDto_whenMakeBucket_thenThrowMinioSecurityException(Throwable exception) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        doAnswer((invocation) -> {
            throw exception;
        }).when(minioClient).makeBucket(makeBucketArgs);

        MinioSecurityException thrownException = assertThrows(MinioSecurityException.class, () -> imageService.upload(imageDto));

        assertEquals(MINIO_SEC_FAILURE_MSG, thrownException.getMessage());
    }

    @ParameterizedTest
    @MethodSource("minioAndIOExceptions")
    void givenWrongImageDto_whenMakeBucket_thenThrowMinioAndIOException(Throwable exception) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        doAnswer((invocation) -> {
            throw exception;
        }).when(minioClient).makeBucket(makeBucketArgs);

        ProcessingImageFailureException thrownException = assertThrows(ProcessingImageFailureException.class,
                                                                       () -> imageService.upload(imageDto));

        assertEquals(MINIO_BUCKET_CREATE_FAILURE_MSG, thrownException.getMessage());
    }

    @ParameterizedTest
    @MethodSource("minioSecurityExceptions")
    void givenWrongImageName_whenGetImageUrl_thenThrowMinioSecurityException(Throwable exception) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        when(minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs)).thenThrow(exception);

        MinioSecurityException thrownException = assertThrows(MinioSecurityException.class,
                                                              () -> imageService.getImageUrl(IMAGE_NAME));

        assertEquals(MINIO_SEC_FAILURE_MSG, thrownException.getMessage());
    }

    public static Stream<Throwable> minioSecurityExceptions() {
        return Stream.of(
                new NoSuchAlgorithmException(),
                new InvalidKeyException()
        );
    }

    public static Stream<Throwable> minioAndIOExceptions() {
        return Stream.of(
                new MinioException(),
                new IOException()
        );
    }
}
