package org.aston.cardservice.mapper;

import org.aston.cardservice.configuration.CardServiceMapperConfig;
import org.aston.cardservice.dto.response.CardProductDto;
import org.aston.cardservice.persistent.entity.CardProduct;
import org.aston.cardservice.service.ImageService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(config = CardServiceMapperConfig.class)
public interface CardProductMapper {

    List<CardProductDto> toCardProductDtoList(List<CardProduct> cardProducts, @Context ImageService imageService);

    default CardProductDto toCardProductDto(CardProduct cardProduct, @Context ImageService imageService) {
        return new CardProductDto(cardProduct.getNameProduct(), imageService.getImageUrl(cardProduct.getImage()));
    }
}
