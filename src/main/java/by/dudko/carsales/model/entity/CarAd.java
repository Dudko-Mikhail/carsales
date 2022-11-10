package by.dudko.carsales.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarAdd implements BaseEntity<Long> { // todo выделять ли машину
    private Long id;
    private Long userId;
    private int year;
    private String brand;
    private String model;
    private Integer engineVolume;
    private CarState carState;
    private Integer mileage;
    private Integer power;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Telephone> telephones;
}
