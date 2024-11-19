package org.aston.cardservice.handler.impl;

import org.aston.cardservice.dto.request.ImageDto;
import org.aston.cardservice.dto.response.CardProductDto;
import org.aston.cardservice.mapper.CardProductMapper;
import org.aston.cardservice.persistent.entity.CardProduct;
import org.aston.cardservice.persistent.enums.CardProductType;
import org.aston.cardservice.service.CardProductService;
import org.aston.cardservice.service.ImageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.aston.cardservice.data.CardProductData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardProductImageHandlerImplTest {

    @InjectMocks
    private CardProductImageHandlerImpl cardProductImageHandler;
    @Mock
    private ImageService imageService;
    @Mock
    private CardProductService cardProductService;
    @Mock
    private CardProductMapper cardProductMapper;

    private static ImageDto imageDto;

    private static CardProductDto cardProductDto;

    private static CardProduct cardProduct;

    @BeforeAll
    public static void initBeforeAll() {
        imageDto = createImageDto();
        cardProductDto = createCardProductDto();
        cardProduct = createCardProduct();
    }

    @Test
    void givenImageDto_whenUploadImage_thenSaveImage() {
        when(imageService.upload(imageDto)).thenReturn(IMAGE_NAME);

        cardProductImageHandler.handleImageUpload(imageDto);

        verify(imageService, times(1)).upload(imageDto);
        verify(cardProductService, times(1)).updateCardProductImage(imageDto, IMAGE_NAME);
    }

    @Test
    void givenCardProductType_whenListAllCardProducts_thenCardProductDtoList() {
        List<CardProduct> cardProductList = List.of(cardProduct);
        List<CardProductDto> cardProductDtoList = List.of((cardProductDto));
        when(cardProductService.getAllCardProducts(CardProductType.DEBIT)).thenReturn(cardProductList);
        when(cardProductMapper.toCardProductDtoList(cardProductList, imageService)).thenReturn(List.of((cardProductDto)));

        List<CardProductDto> resultCardProductDtoList = cardProductImageHandler.handleListCardProducts(CardProductType.DEBIT);

        assertEquals(cardProductDtoList, resultCardProductDtoList);
    }
}
