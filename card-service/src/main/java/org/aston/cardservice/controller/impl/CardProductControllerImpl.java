package org.aston.cardservice.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.aston.cardservice.controller.CardProductController;
import org.aston.cardservice.dto.request.ImageDto;
import org.aston.cardservice.dto.response.CardProductDto;
import org.aston.cardservice.handler.CardProductImageHandler;
import org.aston.cardservice.persistent.enums.CardProductType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.aston.cardservice.configuration.ApplicationConstant.REG_EX_CARD_PRODUCT_TYPE;
import static org.aston.cardservice.configuration.ApplicationConstant.WRONG_CARD_PRODUCT_TYPE_MSG;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/card-service/card-product")
public class CardProductControllerImpl implements CardProductController {

    private final CardProductImageHandler cardProductImageHandler;

    @Override
    @PostMapping("/upload_image")
    public ResponseEntity<String> uploadImage(@Valid @ModelAttribute ImageDto imageDto) {
        cardProductImageHandler.handleImageUpload(imageDto);
        return ResponseEntity.ok("Изображение успешно сохранено");
    }

    @Override
    @GetMapping("/list_card_products")
    public ResponseEntity<List<CardProductDto>> fetchAllCardProducts(
            @Pattern(regexp = REG_EX_CARD_PRODUCT_TYPE, message = WRONG_CARD_PRODUCT_TYPE_MSG)
            @RequestParam(name = "card_product_type") String cardProductType) {
        return ResponseEntity.ok(cardProductImageHandler.handleListCardProducts(CardProductType.valueOf(cardProductType)));
    }
}
