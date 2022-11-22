package by.dudko.carsales.model.dto.carad;

import by.dudko.carsales.model.entity.CarState;
import by.dudko.carsales.model.entity.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarAdDto {
    private Long userId;
    private int year;
    private String brand;
    private String model;
    private Integer engineVolume;
    private CarState carState;
    private Integer mileage;
    private Integer power;
    private List<PhoneNumber> phoneNumbers;
}
