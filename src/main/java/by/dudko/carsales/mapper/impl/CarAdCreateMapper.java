package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.model.dto.carad.CreateCarAdDto;
import by.dudko.carsales.model.entity.CarAd;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarAdCreateMapper implements DtoMapper<CreateCarAdDto, CarAd> {
    private static final CarAdCreateMapper instance = new CarAdCreateMapper();

    public static CarAdCreateMapper getInstance() {
        return instance;
    }

    @Override
    public CarAd map(CreateCarAdDto source) {
        return CarAd.builder()
                .year(source.getYear())
                .brand(source.getBrand())
                .model(source.getModel())
                .engineVolume(source.getEngineVolume())
                .carState(source.getCarState())
                .mileage(source.getMileage())
                .power(source.getPower())
                .userId(source.getUserId())
                .build();
    }
}
