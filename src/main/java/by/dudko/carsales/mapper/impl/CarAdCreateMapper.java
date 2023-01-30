package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.model.dto.carad.CarAdCreateDto;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.model.entity.CarState;
import by.dudko.carsales.model.entity.PhoneNumber;
import by.dudko.carsales.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CarAdCreateMapper implements DtoMapper<CarAdCreateDto, CarAd> {
    private final UserRepository userRepository;

    @Override
    public CarAd map(CarAdCreateDto source) {
        var ad = CarAd.builder()
                .year(source.getYear().getValue())
                .brand(source.getBrand())
                .model(source.getModel())
                .engineVolume(source.getEngineVolume())
                .carState(CarState.valueOf(source.getCarState()))
                .mileage(source.getMileage())
                .power(source.getPower())
                .owner(userRepository.getReferenceById(source.getUserId()))
                .build();
        mapPhoneNumbers(ad, source.getPhoneNumbers());
        return ad;
    }

    private void mapPhoneNumbers(CarAd ad, Collection<String> numbers) {
        List<PhoneNumber> phoneNumbers = numbers.stream()
                .map(n -> PhoneNumber.builder()
                        .number(n)
                        .ad(ad)
                        .build())
                .collect(Collectors.toList());
        ad.setPhoneNumbers(phoneNumbers);
    }
}
