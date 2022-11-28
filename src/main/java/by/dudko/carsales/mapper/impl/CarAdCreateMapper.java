package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.model.dto.carad.CarAdCreateDto;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.model.entity.PhoneNumber;
import by.dudko.carsales.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarAdCreateMapper implements DtoMapper<CarAdCreateDto, CarAd> {
    @Override
    public CarAd map(CarAdCreateDto source) {
        User user = new User();
        user.setId(source.getUserId());
        var ad = CarAd.builder()
                .year(source.getYear().getValue())
                .brand(source.getBrand())
                .model(source.getModel())
                .engineVolume(source.getEngineVolume())
                .carState(source.getCarState())
                .mileage(source.getMileage())
                .power(source.getPower())
                .owner(user)
                .build();
        mapPhoneNumbers(ad, source.getPhoneNumbers());
        return ad;
    }

    private void mapPhoneNumbers(CarAd ad, List<String> numbers) {
        List<PhoneNumber> phoneNumbers = numbers.stream()
                .map(n -> PhoneNumber.builder()
                        .number(n)
                        .ad(ad)
                        .build())
                .collect(Collectors.toList());
        ad.setPhoneNumbers(phoneNumbers);
    }
}
