package by.dudko.carsales.model.dto.carad;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class CarAdReadDto {
    long id;
    int year;
    String brand;
    String model;
    Integer mileage;
    String ownerEmail;
    LocalDateTime createdAt;
    int imagesCount;
}
