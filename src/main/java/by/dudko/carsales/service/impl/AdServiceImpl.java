package by.dudko.carsales.service.impl;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.mapper.impl.CarAdCreateMapper;
import by.dudko.carsales.mapper.impl.CarAdEditMapper;
import by.dudko.carsales.mapper.impl.CarAdReadMapper;
import by.dudko.carsales.model.dto.carad.CarAdCreateDto;
import by.dudko.carsales.model.dto.carad.CarAdEditDto;
import by.dudko.carsales.model.dto.carad.CarAdReadDto;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.repository.CarAdRepository;
import by.dudko.carsales.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final CarAdRepository carAdRepository;
    private final CarAdCreateMapper adCreateMapper;
    private final CarAdReadMapper adReadMapper;
    private final CarAdEditMapper adEditMapper;

    @Override
    public <T> Page<T> findAll(DtoMapper<CarAd, T> mapper, Pageable pageable) {
        return carAdRepository.findAll(pageable)
                .map(mapper::map);
    }

    @Override
    public <T> Optional<T> findById(long adId, DtoMapper<CarAd, T> mapper) {
        return carAdRepository.findById(adId)
                .map(mapper::map);
    }

    @Override
    @Transactional
    public CarAdReadDto saveAd(CarAdCreateDto adDto) {
        return Optional.of(adDto)
                .map(adCreateMapper::map)
                .map(carAdRepository::saveAndFlush)
                .map(adReadMapper::map)
                .orElseThrow();
    }

    @Override
    public Optional<CarAdReadDto> updateAd(long adId, CarAdEditDto carAdDto) {
        return carAdRepository.findById(adId)
                .map(ad -> adEditMapper.map(carAdDto, ad))
                .map(carAdRepository::saveAndFlush)
                .map(adReadMapper::map);
    }

    @Override
    @Transactional
    public boolean deleteById(long adId) {
        return carAdRepository.findById(adId)
                .map(ad -> {
                    carAdRepository.deleteById(adId);
                    carAdRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
