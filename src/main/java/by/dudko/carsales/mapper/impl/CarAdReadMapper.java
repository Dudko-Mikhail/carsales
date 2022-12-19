package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.model.dto.carad.CarAdReadDto;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.repository.AdImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarAdReadMapper implements DtoMapper<CarAd, CarAdReadDto> {
    private final AdImageRepository imageRepository;

    @Override
    public CarAdReadDto map(CarAd source) {
        long adId = source.getId();
        return CarAdReadDto.builder()
                .id(adId)
                .ownerEmail(source.getOwner().getEmail())
                .year(source.getYear())
                .model(source.getModel())
                .brand(source.getBrand())
                .mileage(source.getMileage())
                .createdAt(source.getCreatedAt())
                .imagesCount(imageRepository.countByAdId(adId))
                .build();
    }
}
