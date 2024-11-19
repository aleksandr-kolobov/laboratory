package org.aston.cardservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.aston.cardservice.dto.request.ImageDto;
import org.aston.cardservice.dto.response.CardProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.aston.cardservice.configuration.ApplicationConstant.REG_EX_CARD_PRODUCT_TYPE;
import static org.aston.cardservice.configuration.ApplicationConstant.WRONG_CARD_PRODUCT_TYPE_MSG;

@Tag(name = "Контроллер для работы с карточными продуктами", description = "Card API version v1")
public interface CardProductController {

    @Operation(
            summary = "загрузка изображения",
            description = "загрузить изображение и сохранить имя изображения у соответствующего карточного продукта",
            tags = {"card-product"}
    )
    ResponseEntity<String> uploadImage(@Valid @ModelAttribute ImageDto imageDto);


    @Operation(
            summary = "Получить карточные продуты",
            description = "Получить карточные продуты соответствующего типа",
            tags = {"card-product"}
    )
    ResponseEntity<List<CardProductDto>> fetchAllCardProducts(@Pattern(regexp = REG_EX_CARD_PRODUCT_TYPE, message = WRONG_CARD_PRODUCT_TYPE_MSG) @RequestParam(name = "card_product_type") String cardProductType);
}
