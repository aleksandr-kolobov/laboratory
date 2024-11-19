package org.aston.cardservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.aston.cardservice.validation.ImageFile;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class ImageDto {

    @NotBlank(message = "Отсутствует параметр")
    String cardProductId;

    @ImageFile
    private MultipartFile file;
}
