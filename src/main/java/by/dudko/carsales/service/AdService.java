package by.dudko.carsales.service;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.model.dto.carad.CarAdCreateDto;
import by.dudko.carsales.model.dto.carad.CarAdEditDto;
import by.dudko.carsales.model.dto.carad.CarAdReadDto;
import by.dudko.carsales.model.dto.user.UserReadDto;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.model.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AdService {
    <T> Page<T> findAll(DtoMapper<CarAd, T> mapper, Pageable pageable);

    <T> Optional<T> findById(long adId, DtoMapper<CarAd, T> mapper);

    Optional<UserReadDto> findAdOwner(long adId);

    CarAdReadDto saveAd(CarAdCreateDto carAdDto);

    Optional<List<Image>> uploadImages(long adId, List<MultipartFile> images);

    Optional<CarAdReadDto> updateAd(long adId, CarAdEditDto carAdDto);

    Optional<byte[]> findAdImageById(long imageId);

    boolean deleteImageById(long imageId);

    boolean deleteById(long adId);
}
