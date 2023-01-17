package by.dudko.carsales.integration.service;

import by.dudko.carsales.integration.BaseIntegrationTest;
import by.dudko.carsales.model.dto.carad.CarAdCreateDto;
import by.dudko.carsales.model.dto.carad.CarAdEditDto;
import by.dudko.carsales.model.dto.carad.CarAdFullReadDto;
import by.dudko.carsales.model.dto.carad.CarAdReadDto;
import by.dudko.carsales.model.dto.user.UserReadDto;
import by.dudko.carsales.model.entity.CarState;
import by.dudko.carsales.model.entity.Image;
import by.dudko.carsales.service.AdService;
import by.dudko.carsales.utils.ImageStorageManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class AdServiceTest extends BaseIntegrationTest {
    private static final long AD_ID = 1L;
    private static final long NON_EXISTENT_ID = 100L;
    private static final long USER_ID = 1L;
    private static final long NON_EXISTENT_USER_ID = 200L;
    private static final long IMAGE_ID = 1L;
    private static final long NON_EXISTENT_IMAGE_ID = 1000L;
    private static final long TOTAL_ADS_NUMBER = 3;

    private final AdService adService;
    private final ImageStorageManager storageManager;

    @Test
    void findAll() {
        Page<CarAdReadDto> page = adService.findAll(PageRequest.of(0, 5));
        List<CarAdReadDto> content = page.getContent();
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(TOTAL_ADS_NUMBER);
        assertThat(content).hasSize(3);
    }

    @Test
    void findAllWithFullData() {
        int pageSize = 2;
        Page<CarAdFullReadDto> page = adService.findAllWithFullData(PageRequest.of(0, pageSize));
        List<CarAdFullReadDto> content = page.getContent();
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(TOTAL_ADS_NUMBER);
        assertThat(content).hasSize(pageSize);
    }

    @Test
    void findAllByOwnerId() {
        Optional<List<CarAdReadDto>> userAds = adService.findAllByOwnerId(USER_ID);
        assertThat(userAds).isPresent();
        userAds.ifPresent(ads -> {
            assertThat(ads).hasSize(1);
            var ad = ads.get(0);
            assertThat(ad.getId()).isEqualTo(1L);
        });
    }

    @Test
    void findAllByOwnerIdWithNonExistentOwner() {
        assertThat(adService.findAllByOwnerId(NON_EXISTENT_USER_ID)).isEmpty();
    }

    @Test
    void findAllByOwnerIdWithFullData() {
        Optional<List<CarAdFullReadDto>> userAds = adService.findAllByOwnerIdWithFullData(USER_ID);
        assertThat(userAds).isPresent();
        userAds.ifPresent(ads -> {
            assertThat(ads).hasSize(1);
            var ad = ads.get(0);
            assertThat(ad.getId()).isEqualTo(1L);
        });
    }

    @Test
    void findById() {
        Optional<CarAdReadDto> maybeAd = adService.findById(AD_ID);
        assertThat(maybeAd).isPresent();
        maybeAd.ifPresent(ad -> {
            assertThat(ad.getId()).isEqualTo(AD_ID);
            assertThat(ad.getYear()).isEqualTo(2022);
            assertThat(ad.getBrand()).isEqualTo("BMW");
            assertThat(ad.getModel()).isEqualTo("X6");
            assertThat(ad.getMileage()).isEqualTo(1000);
            assertThat(ad.getOwnerEmail()).isEqualTo("ivan@mail.ru");
            assertThat(ad.getImagesCount()).isEqualTo(2);
            assertThat(ad.getCreatedAt()).isEqualTo(LocalDateTime.of(2021, 12, 3, 13, 24));
        });
    }

    @Test
    void findByIdWithFullData() {
        Optional<CarAdFullReadDto> maybeAd = adService.findByIdWithFullData(AD_ID);
        assertThat(maybeAd).isPresent();
        maybeAd.ifPresent(ad -> {
            assertThat(ad.getId()).isEqualTo(AD_ID);
            assertThat(ad.getYear()).isEqualTo(2022);
            assertThat(ad.getBrand()).isEqualTo("BMW");
            assertThat(ad.getModel()).isEqualTo("X6");
            assertThat(ad.getMileage()).isEqualTo(1000);
            assertThat(ad.getOwnerEmail()).isEqualTo("ivan@mail.ru");
            assertThat(ad.getPhoneNumbers()).containsExactlyInAnyOrder("375254781236", "375297221462");
            assertThat(ad.getImageLinks()).containsExactlyInAnyOrder("/ads/images/1", "/ads/images/2");
            assertThat(ad.getCreatedAt()).isEqualTo(LocalDateTime.of(2021, 12, 3, 13, 24));
        });
    }

    @Test
    void findAdOwner() {
        Optional<UserReadDto> adOwner = adService.findAdOwner(AD_ID);
        assertThat(adOwner).isPresent();
        adOwner.ifPresent(user -> {
            assertThat(user.getId()).isEqualTo(1L);
            assertThat(user.getEmail()).isEqualTo("ivan@mail.ru");
            assertThat(user.getPhoneNumber()).isEqualTo("375254781236");
            assertThat(user.getCreatedAt()).isEqualTo(LocalDateTime.of(2021, 12, 1, 12, 00));
        });
    }

    @Test
    void findAdOwnerWithNonExistentAd() {
        assertThat(adService.findAdOwner(NON_EXISTENT_ID)).isEmpty();
    }

    @Test
    void saveAd() {
        CarAdCreateDto createDto = CarAdCreateDto.builder()
                .userId(2L)
                .year(Year.of(2020))
                .brand("Good")
                .model("XW9")
                .engineVolume(68)
                .carState(CarState.NEW.name())
                .mileage(0)
                .power(640)
                .phoneNumbers(Set.of("777", "888", "999"))
                .build();

        long adId = adService.saveAd(createDto).getId();
        Optional<CarAdFullReadDto> newAd = adService.findByIdWithFullData(adId);

        assertThat(newAd).isPresent();
        newAd.ifPresent(ad -> {
            assertThat(ad.getOwnerEmail()).isEqualTo("test_user@gmail.com");
            assertThat(ad.getYear()).isEqualTo(createDto.getYear().getValue());
            assertThat(ad.getBrand()).isEqualTo(createDto.getBrand());
            assertThat(ad.getModel()).isEqualTo(createDto.getModel());
            assertThat(ad.getEngineVolume()).isEqualTo(createDto.getEngineVolume());
            assertThat(ad.getCarState()).isSameAs(CarState.NEW);
            assertThat(ad.getMileage()).isEqualTo(createDto.getMileage());
            assertThat(ad.getPower()).isEqualTo(createDto.getPower());
            assertThat(ad.getPhoneNumbers()).containsExactlyInAnyOrder("777", "888", "999");
        });
    }

    @Test
    void uploadImages() {
        byte[] bytes = new byte[]{124, 102, 104, 106, 115};
        String fileName = "test.png";
        MultipartFile testFile = new MockMultipartFile(fileName, fileName, null, bytes);

        Optional<List<Image>> maybeImages = adService.uploadImages(3, List.of(testFile));
        assertThat(maybeImages).isPresent();

        var images = maybeImages.get();
        assertThat(images).hasSize(1);

        Image image = images.get(0);
        assertThat(image.getImageName()).isEqualTo(fileName);

        Optional<byte[]> imageBytes = adService.findAdImageById(image.getId());
        assertThat(imageBytes).isPresent();
        imageBytes.ifPresent(data -> assertThat(data).isEqualTo(bytes));
    }

    @Test
    void uploadImagesToNonExistentAd() {
        MultipartFile testFile = new MockMultipartFile("test.jpg", new byte[]{124, 120, 119});
        assertThat(adService.uploadImages(NON_EXISTENT_ID, List.of(testFile))).isEmpty();
    }

    @Test
    void updateAd() {
        CarAdEditDto editDto = CarAdEditDto.builder()
                .year(Year.of(2020))
                .brand("TestBrand")
                .model("TestModel")
                .engineVolume(200)
                .mileage(999)
                .power(1000)
                .build();

        Optional<CarAdReadDto> updatedAd = adService.updateAd(AD_ID, editDto);

        assertThat(updatedAd).isPresent();
        updatedAd.ifPresent(ad -> {
            assertThat(ad.getYear()).isEqualTo(editDto.getYear().getValue());
            assertThat(ad.getBrand()).isEqualTo(editDto.getBrand());
            assertThat(ad.getModel()).isEqualTo(editDto.getModel());
            assertThat(ad.getMileage()).isEqualTo(editDto.getMileage());
        });
    }

    @Test
    void updateAdWithNonExistentId() {
        CarAdEditDto editDto = CarAdEditDto.builder()
                .year(Year.of(2020))
                .brand("TestBrand")
                .model("TestModel")
                .engineVolume(200)
                .mileage(999)
                .power(1000)
                .build();

        assertThat(adService.updateAd(NON_EXISTENT_ID, editDto)).isEmpty();
    }

    @Test
    @Order(1)
    void findAdImageById() {
        storageManager.fillImageStorage();
        assertThat(adService.findAdImageById(1L)).isPresent();
    }


    @Test
    void findAdImageByIdWithExistingDbRowAndNonExistentImageFile() {
        assertThrows(RuntimeException.class, () -> adService.findAdImageById(3),
                "Image not found but exists in db");
    }

    @Test
    void findAdImageByIdWithNonExistentImage() {
        assertThat(adService.findAdImageById(NON_EXISTENT_IMAGE_ID)).isEmpty();
    }

    @Test
    @Order(2)
    void deleteImageById() {
        assertTrue(adService.deleteImageById(IMAGE_ID));
        assertThat(adService.findAdImageById(IMAGE_ID)).isEmpty();
    }

    @Test
    void deleteImageByIdWithNonExistentId() {
        assertFalse(adService.deleteImageById(NON_EXISTENT_IMAGE_ID));
    }

    @Test
    void deleteById() {
        storageManager.fillImageStorage();
        assertTrue(adService.deleteById(AD_ID));
        assertThat(adService.findById(AD_ID)).isEmpty();
    }

    @Test
    void deleteByIdWithNonExistentId() {
        assertFalse(adService.deleteById(NON_EXISTENT_ID));
    }

    @PreDestroy
    private void clearImageStorage() {
        storageManager.clearImageStorage();
    }
}