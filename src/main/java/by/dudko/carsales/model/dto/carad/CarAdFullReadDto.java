package by.dudko.carsales.model.dto.carad;

import by.dudko.carsales.model.entity.CarState;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class CarAdFullReadDto {
    long id;
    int year;
    String brand;
    String model;
    Integer engineVolume;
    Integer power;
    CarState carState;
    Integer mileage;
    String ownerEmail;
    List<String> phoneNumbers;
    List<String> imageUrls;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
