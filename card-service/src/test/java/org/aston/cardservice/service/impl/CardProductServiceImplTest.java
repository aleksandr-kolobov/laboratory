package org.aston.cardservice.service.impl;

import org.aston.cardservice.dto.request.ImageDto;
import org.aston.cardservice.dto.response.CardProductDto;
import org.aston.cardservice.exception.CardServiceGenericException;
import org.aston.cardservice.persistent.entity.CardProduct;
import org.aston.cardservice.persistent.enums.CardProductType;
import org.aston.cardservice.persistent.repository.CardProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.aston.cardservice.data.CardProductData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardProductServiceImplTest {

    @InjectMocks
    private CardProductServiceImpl cardProductService;
    @Mock
    private CardProductRepository cardProductRepository;

    private static CardProduct cardProduct;

    private static ImageDto imageDto;

    private static CardProductDto cardProductDto;

    @BeforeAll
    public static void init() {
        cardProductDto = createCardProductDto();
        cardProduct = createCardProduct();
        imageDto = createImageDto();
    }

    @Test
    void givenCardProductType_whenListAllCardsWithImages_thenReturnCardProductDtoList() {
        List<CardProduct> cardProductList = List.of(cardProduct);
        when(cardProductRepository.findByTypeCard(CardProductType.DEBIT)).thenReturn(cardProductList);

        List<CardProduct> resultCardProductList = cardProductService.getAllCardProducts(CardProductType.DEBIT);

        Assertions.assertEquals(cardProductList, resultCardProductList);
        verify(cardProductRepository, times(1)).findByTypeCard(CardProductType.DEBIT);
    }

    @Test
    void givenWrongCardProductType_whenListAllCardsWithImages_thenThrowCardServiceGenericException() {
        when(cardProductRepository.findByTypeCard(any())).thenReturn(Collections.emptyList());

        CardServiceGenericException thrownException = assertThrows(CardServiceGenericException.class,
                                                                   () -> cardProductService.getAllCardProducts(any()));
        assertEquals("Отсутствуют данные о карточных продуктах", thrownException.getMessage());
    }

    @Test
    void givenImageDto_whenSaveImage_thenSaveCardProduct() {
        cardProduct.setCardProductId(CARD_PRODUCT_ID_VALUE);
        when(cardProductRepository.findById(imageDto.getCardProductId())).thenReturn(Optional.of(cardProduct));

        cardProductService.updateCardProductImage(imageDto, IMAGE_NAME);

        verify(cardProductRepository, times(1)).findById(imageDto.getCardProductId());
        verify(cardProductRepository, times(1)).save(cardProduct);
    }

    @Test
    void givenWrongCardProductId_whenSaveImage_thenThrowCardServiceGenericException() {
        when(cardProductRepository.findById(imageDto.getCardProductId()))
                .thenThrow(new CardServiceGenericException(HttpStatus.NOT_FOUND, "Карточный продукт не найден"));

        CardServiceGenericException thrownException = assertThrows(CardServiceGenericException.class,
                                                                   () -> cardProductService.updateCardProductImage(imageDto, IMAGE_NAME));

        assertEquals("Карточный продукт не найден", thrownException.getMessage());
    }
}
