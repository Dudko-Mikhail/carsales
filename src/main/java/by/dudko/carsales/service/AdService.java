package by.dudko.carsales.service;

import by.dudko.carsales.model.dto.carad.CarAdCreateDto;
import by.dudko.carsales.model.dto.carad.CarAdEditDto;
import by.dudko.carsales.model.dto.carad.CarAdFullReadDto;
import by.dudko.carsales.model.dto.carad.CarAdReadDto;
import by.dudko.carsales.model.dto.image.ImageReadDto;
import by.dudko.carsales.model.dto.user.UserReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AdService {
    Page<CarAdReadDto> findAll(Pageable pageable);

    Page<CarAdFullReadDto> findAllWithFullData(Pageable pageable);

    Optional<List<CarAdReadDto>> findAllByOwnerId(long userId);

    Optional<List<CarAdFullReadDto>> findAllByOwnerIdWithFullData(long userId);

    Optional<CarAdReadDto> findById(long adId);

    Optional<CarAdFullReadDto> findByIdWithFullData(long adId);

    Optional<UserReadDto> findAdOwner(long adId);

    CarAdReadDto saveAd(CarAdCreateDto carAdDto);

    Optional<List<ImageReadDto>> uploadImages(long adId, List<MultipartFile> images);

    Optional<CarAdReadDto> updateAd(long adId, CarAdEditDto carAdDto);

    Optional<byte[]> findAdImageById(long imageId);

    boolean deleteImageById(long imageId);

    boolean deleteById(long adId);
}
