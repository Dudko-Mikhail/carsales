package by.dudko.carsales.service.impl;

import by.dudko.carsales.mapper.impl.CarAdCreateMapper;
import by.dudko.carsales.mapper.impl.CarAdEditMapper;
import by.dudko.carsales.mapper.impl.CarAdFullInfoReadMapper;
import by.dudko.carsales.mapper.impl.CarAdReadMapper;
import by.dudko.carsales.mapper.impl.UserReadMapper;
import by.dudko.carsales.model.dto.carad.CarAdCreateDto;
import by.dudko.carsales.model.dto.carad.CarAdEditDto;
import by.dudko.carsales.model.dto.carad.CarAdFullReadDto;
import by.dudko.carsales.model.dto.carad.CarAdReadDto;
import by.dudko.carsales.model.dto.user.UserReadDto;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.model.entity.Image;
import by.dudko.carsales.repository.AdImageRepository;
import by.dudko.carsales.repository.CarAdRepository;
import by.dudko.carsales.repository.UserRepository;
import by.dudko.carsales.service.AdService;
import by.dudko.carsales.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final CarAdRepository carAdRepository;
    private final AdImageRepository imageRepository;
    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final ImageService imageService;
    private final CarAdCreateMapper adCreateMapper;
    private final CarAdReadMapper adReadMapper;
    private final CarAdFullInfoReadMapper adFullInfoReadMapper;
    private final CarAdEditMapper adEditMapper;

    @Override
    public Page<CarAdReadDto> findAll(Pageable pageable) {
        return carAdRepository.findAll(pageable)
                .map(adReadMapper::map);
    }

    @Override
    public Page<CarAdFullReadDto> findAllWithFullData(Pageable pageable) {
        return carAdRepository.findAll(pageable)
                .map(adFullInfoReadMapper::map);
    }

    @Override
    public Optional<List<CarAdReadDto>> findAllByOwnerId(long userId) {
        if (!userRepository.existsById(userId)) {
            return Optional.empty();
        }
        return Optional.of(carAdRepository.findAllByOwnerId(userId).stream()
                .map(adReadMapper::map)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<CarAdFullReadDto>> findAllByOwnerIdWithFullData(long userId) {
        if (!userRepository.existsById(userId)) {
            return Optional.empty();
        }
        return Optional.of(carAdRepository.findAllByOwnerId(userId).stream()
                .map(adFullInfoReadMapper::map)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<CarAdReadDto> findById(long adId) {
        return carAdRepository.findById(adId)
                .map(adReadMapper::map);
    }

    @Override
    public Optional<CarAdFullReadDto> findByIdWithFullData(long adId) {
        return carAdRepository.findById(adId)
                .map(adFullInfoReadMapper::map);
    }

    @Override
    public Optional<UserReadDto> findAdOwner(long adId) {
        return carAdRepository.findById(adId)
                .map(CarAd::getOwner)
                .map(userReadMapper::map);
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
    @Transactional
    public Optional<List<Image>> uploadImages(long adId, List<MultipartFile> images) {
        return carAdRepository.findById(adId)
                .map(ad -> {
                    List<Image> imageList = new ArrayList<>();
                    images.forEach(imageFile -> {
                        Image image = uploadImage(adId, imageFile);
                        imageList.add(image);
                        modifyUpdatedAt(image.getAd());
                    });
                    return imageList;
                });
    }

    @SneakyThrows
    private Image uploadImage(long adId, MultipartFile imageFile) {
        String imageName = imageFile.getOriginalFilename();
        CarAd ad = carAdRepository.getReferenceById(adId);
        Image image = imageRepository.save(Image.of(ad, imageName));
        imageService.uploadImage(image.defineImagePath(), imageFile.getInputStream());
        return image;
    }

    @Override
    @Transactional
    public Optional<CarAdReadDto> updateAd(long adId, CarAdEditDto carAdDto) {
        return carAdRepository.findById(adId)
                .map(ad -> adEditMapper.map(carAdDto, ad))
                .map(adReadMapper::map);
    }

    @Override
    public Optional<byte[]> findAdImageById(long imageId) {
        return imageRepository.findById(imageId)
                .map(image -> imageService.loadImage(image.defineImagePath())
                        .orElseThrow(() -> new RuntimeException("Image not found but exists in db")));
    }

    @Override
    @Transactional
    public boolean deleteImageById(long imageId) {
        return imageRepository.findById(imageId)
                .map(image -> {
                    imageService.deleteImage(image.defineImagePath());
                    imageRepository.deleteById(imageId);
                    modifyUpdatedAt(image.getAd());
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean deleteById(long adId) {
        return carAdRepository.findById(adId)
                .map(ad -> {
                    carAdRepository.deleteById(adId);
                    ad.getImages()
                            .forEach(image -> imageService.deleteImage(image.defineImagePath()));
                    carAdRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    private void modifyUpdatedAt(CarAd ad) {
        ad.setUpdatedAt(LocalDateTime.now());
    }
}
