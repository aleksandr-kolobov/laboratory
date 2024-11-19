package org.aston.cardservice.mapper;

import org.aston.cardservice.dto.response.CardProductDto;
import org.aston.cardservice.persistent.entity.CardProduct;
import org.aston.cardservice.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.aston.cardservice.data.CardProductData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardProductMapperTest {

    private CardProductMapper cardProductMapper;
    @Mock
    private ImageService imageService;

    @BeforeEach
    public void setUp() {
        cardProductMapper = new CardProductMapperImpl();
    }

    @Test
    void givenCardCardProducts_whenTtoCardProductDtoList_thenCardProductDtoList() {
        List<CardProduct> cardProducts = List.of(createCardProduct());
        List<CardProductDto> cardProductsDto = List.of(createCardProductDto());
        when(imageService.getImageUrl(any())).thenReturn(IMAGE_URL);

        List<CardProductDto> cardProductDtoListResult = cardProductMapper.toCardProductDtoList(cardProducts, imageService);

        assertEquals(cardProductsDto.get(0).getNameProduct(), cardProductDtoListResult.get(0).getNameProduct());
        assertEquals(cardProductsDto.get(0).getImageUrl(), cardProductDtoListResult.get(0).getImageUrl());
    }
}


