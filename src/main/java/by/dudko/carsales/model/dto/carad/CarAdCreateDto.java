package by.dudko.carsales.model.dto.carad;

import by.dudko.carsales.model.entity.CarState;
import by.dudko.carsales.validation.annotation.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.Year;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarAdCreateDto {
    @NotNull
    private Long userId;

    @NotNull
    @PastOrPresent
    private Year year;

    @NotEmpty
    private String brand;

    @NotEmpty
    private String model;

    @Positive
    private Integer engineVolume;

    @NotEmpty
    @ValueOfEnum(enumClass = CarState.class)
    private String carState;

    @PositiveOrZero
    private Integer mileage;

    @Positive
    private Integer power;

    @Size(min = 1, max = 3)
    private Set<String> phoneNumbers;
}
