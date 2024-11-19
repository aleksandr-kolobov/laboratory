package org.aston.cardservice.controller.impl.integration;

import org.aston.cardservice.exception.ProcessingImageFailureException;
import org.aston.cardservice.service.CardProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.aston.cardservice.data.CardProductData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecondaryProductControllerImplTest extends AbstractCardProductControllerImplTest {
/*
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CardProductService cardProductService;

    @Test
    void givenCardProductType_whenListAllCardsWithImages_thenServerError() throws Exception {
        when(cardProductService.getAllCardProducts(any())).thenThrow(new ProcessingImageFailureException(IMAGE_URL_FAILURE));

        mockMvc.perform(get("/api/v1/card-service/card-product/list_card_products")
                                .param(CARD_PRODUCT_TYPE_PARAM, CARD_PRODUCT_TYPE_VALUE))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(result -> result
                        .getResponse()
                        .getContentAsString().contains(IMAGE_URL_FAILURE));
    }
*/
}
