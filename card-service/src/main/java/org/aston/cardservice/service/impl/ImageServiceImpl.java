package org.aston.cardservice.service.impl;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.cardservice.dto.request.ImageDto;
import org.aston.cardservice.exception.MinioSecurityException;
import org.aston.cardservice.exception.ProcessingImageFailureException;
import org.aston.cardservice.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.aston.cardservice.configuration.ApplicationConstant.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public String upload(ImageDto image) {
        createBucket();
        MultipartFile file = image.getFile();
        String fileName = generateFileName(file);
        try (InputStream inputStream = file.getInputStream()) {
            saveImage(inputStream, fileName);
        } catch (IOException e) {
            throw new ProcessingImageFailureException(MINIO_IMG_SAVE_SUCCESS_MSG);
        }
        return fileName;
    }

    @Override
    public String getImageUrl(String imageName) {
        try {
            String imageUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(imageName)
                            .expiry(IMG_EXPIRY)
                            .build()
            );
            log.info("Успешно получен url изображения: {}", imageName);
            return imageUrl;
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new MinioSecurityException(MINIO_SEC_FAILURE_MSG);
        } catch (MinioException | IOException ex) {
            throw new ProcessingImageFailureException(MINIO_GET_IMAGE_URL_FAILURE_MSG);
        }
    }

    private void createBucket() {
        boolean found = false;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            log.info("Подтверждено существование бакета с именем: {}", bucket);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new MinioSecurityException(MINIO_SEC_FAILURE_MSG);
        } catch (MinioException | IOException ex) {
            throw new ProcessingImageFailureException(MINIO_BUCKET_EXISTS_FAILURE_MSG);
        }
        if (!found) {
            try {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucket)
                                .build()
                );
                log.info("Успешно создан бакет: {}", bucket);
            } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
                throw new MinioSecurityException(MINIO_SEC_FAILURE_MSG);
            } catch (MinioException | IOException ex) {
                throw new ProcessingImageFailureException(MINIO_BUCKET_CREATE_FAILURE_MSG);
            }
        }
    }

    private void saveImage(InputStream inputStream, String fileName) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .stream(inputStream, inputStream.available(), -1)
                            .bucket(bucket)
                            .object(fileName)
                            .build()
            );
            log.info("Успешно добавлено изображение: {}", fileName);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new MinioSecurityException(MINIO_SEC_FAILURE_MSG);
        } catch (MinioException | IOException ex) {
            throw new ProcessingImageFailureException(MINIO_IMG_SAVE_FAILURE_MSG);
        }
    }

    private String generateFileName(MultipartFile file) {
        String extension = getExtension(file);
        return UUID.randomUUID() + FILE_EXTENSION_DELIMITER + extension;
    }

    private String getExtension(MultipartFile file) {
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(FILE_EXTENSION_DELIMITER) + 1);
    }
}
