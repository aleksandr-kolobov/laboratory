package org.aston.cardservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.cardservice.dto.request.ImageDto;
import org.aston.cardservice.exception.CardServiceGenericException;
import org.aston.cardservice.persistent.entity.CardProduct;
import org.aston.cardservice.persistent.enums.CardProductType;
import org.aston.cardservice.persistent.repository.CardProductRepository;
import org.aston.cardservice.service.CardProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardProductServiceImpl implements CardProductService {

    private final CardProductRepository cardProductRepository;

    @Override
    public List<CardProduct> getAllCardProducts(CardProductType cardProductType) {
        List<CardProduct> cardProducts = cardProductRepository.findByTypeCard(cardProductType);
        if (cardProducts.isEmpty()) {
            throw new CardServiceGenericException(HttpStatus.NOT_FOUND, "Отсутствуют данные о карточных продуктах");
        } else {
            log.info("Успешно получены карточные продукты типа : {}", cardProductType);
            return cardProducts;
        }
    }

    @Override
    public void updateCardProductImage(ImageDto imageDto, String imageName) {
        String cardProductId = imageDto.getCardProductId();
        CardProduct cardProduct = cardProductRepository.findById(cardProductId)
                .orElseThrow(() -> new CardServiceGenericException(HttpStatus.NOT_FOUND, "Карточный продукт не найден"));
        log.info("Успешно найден карточный продукт с id: {}", cardProductId);
        cardProduct.setImage(imageName);
        cardProductRepository.save(cardProduct);
        log.info("Успешно сохранен карточный продукт с именем изображения: {}", imageName);
    }
}
