package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.model.dto.carad.CarAdReadDto;
import by.dudko.carsales.model.entity.CarAd;
import org.springframework.stereotype.Component;

@Component
public class CarAdReadMapper implements DtoMapper<CarAd, CarAdReadDto> {
    @Override
    public CarAdReadDto map(CarAd source) {
        return CarAdReadDto.builder()
                .id(source.getId())
                .ownerEmail(source.getOwner().getEmail())
                .year(source.getYear())
                .model(source.getModel())
                .brand(source.getBrand())
                .mileage(source.getMileage())
                .createdAt(source.getCreatedAt())
//                .imagesCount() // todo add images count
                .build();
    }
}
