package by.dudko.carsales.service;

import by.dudko.carsales.model.dto.carad.CreateCarAdDto;
import by.dudko.carsales.model.entity.CarAd;

import java.util.List;
import java.util.Optional;

public interface AdService {
    List<CarAd> findAll();
    Optional<CarAd> findById(long adId);
    boolean deleteById(long adId);
    Optional<CarAd> saveAd(CreateCarAdDto carAdDto);
}
