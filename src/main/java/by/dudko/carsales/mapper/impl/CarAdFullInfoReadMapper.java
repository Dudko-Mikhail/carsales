package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.model.dto.carad.CarAdFullReadDto;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.model.entity.PhoneNumber;
import by.dudko.carsales.repository.PhoneNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CarAdFullInfoReadMapper implements DtoMapper<CarAd, CarAdFullReadDto> {
    private final PhoneNumberRepository phoneNumberRepository;

    @Override
    public CarAdFullReadDto map(CarAd source) {
        long adId = source.getId();
        return CarAdFullReadDto.builder()
                .id(adId)
                .year(source.getYear())
                .brand(source.getBrand())
                .model(source.getModel())
                .engineVolume(source.getEngineVolume())
                .power(source.getPower())
                .carState(source.getCarState())
                .mileage(source.getMileage())
                .ownerEmail(source.getOwner().getEmail())
                .phoneNumbers(mapPhoneNumbers(phoneNumberRepository.findAllByAdId(adId)))
//                .imageUrls() // todo add after adding images
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }

    private List<String> mapPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        return phoneNumbers.stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.toList());
    }
}
