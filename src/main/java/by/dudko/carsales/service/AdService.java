package by.dudko.carsales.service;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.model.dto.carad.CarAdCreateDto;
import by.dudko.carsales.model.dto.carad.CarAdEditDto;
import by.dudko.carsales.model.dto.carad.CarAdReadDto;
import by.dudko.carsales.model.entity.CarAd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdService {
    <T> Page<T> findAll(DtoMapper<CarAd, T> mapper, Pageable pageable);
    <T> Optional<T> findById(long adId, DtoMapper<CarAd, T> mapper);
    CarAdReadDto saveAd(CarAdCreateDto carAdDto);
    Optional<CarAdReadDto> updateAd(long adId, CarAdEditDto carAdDto);
    boolean deleteById(long adId);
}
