package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.DtoMapperWithTargetObject;
import by.dudko.carsales.model.dto.carad.CarAdEditDto;
import by.dudko.carsales.model.entity.CarAd;
import org.springframework.stereotype.Component;

@Component
public class CarAdEditMapper implements DtoMapperWithTargetObject<CarAdEditDto, CarAd> {
    @Override
    public CarAd map(CarAdEditDto source, CarAd target) {
        target.setYear(source.getYear().getValue());
        target.setBrand(source.getBrand());
        target.setModel(source.getModel());
        target.setEngineVolume(source.getEngineVolume());
        target.setMileage(source.getMileage());
        target.setPower(source.getPower());
        return target;
    }
}
