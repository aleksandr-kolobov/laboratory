package org.aston.cardservice.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.cardservice.dto.request.ImageDto;
import org.aston.cardservice.dto.response.CardProductDto;
import org.aston.cardservice.handler.CardProductImageHandler;
import org.aston.cardservice.mapper.CardProductMapper;
import org.aston.cardservice.persistent.entity.CardProduct;
import org.aston.cardservice.persistent.enums.CardProductType;
import org.aston.cardservice.service.CardProductService;
import org.aston.cardservice.service.ImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardProductImageHandlerImpl implements CardProductImageHandler {

    private final CardProductService cardProductService;
    private final ImageService imageService;
    private final CardProductMapper cardProductMapper;

    @Override
    public void handleImageUpload(ImageDto imageDto) {
        String imageName = imageService.upload(imageDto);
        cardProductService.updateCardProductImage(imageDto, imageName);
    }

    @Override
    public List<CardProductDto> handleListCardProducts(CardProductType cardProductType) {
        List<CardProduct> cardProducts = cardProductService.getAllCardProducts(cardProductType);
        String cardProductClass = CardProduct.class.getSimpleName();
        log.info("Начало отображения {} List в DTO", cardProductClass);
        List<CardProductDto> cardProductDtoList = cardProductMapper.toCardProductDtoList(cardProducts, imageService);
        log.info("Успешное отображения {} List в {} List", cardProductClass, CardProductDto.class.getSimpleName());
        return cardProductDtoList;
    }
}
