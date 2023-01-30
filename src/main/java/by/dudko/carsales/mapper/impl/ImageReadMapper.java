package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.model.dto.image.ImageReadDto;
import by.dudko.carsales.model.entity.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageReadMapper implements DtoMapper<Image, ImageReadDto> {
    @Override
    public ImageReadDto map(Image source) {
        return ImageReadDto.builder()
                .id(source.getId())
                .adId(source.getAd().getId())
                .imageName(source.getImageName())
                .build();
    }
}
