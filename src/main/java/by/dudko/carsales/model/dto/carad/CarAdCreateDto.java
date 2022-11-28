package by.dudko.carsales.model.dto.carad;

import by.dudko.carsales.model.entity.CarState;
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
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarAdCreateDto {
    @NotNull
    private Long userId;

    @PastOrPresent
    private Year year;

    @NotEmpty
    private String brand;

    @NotEmpty
    private String model;

    @Positive
    private Integer engineVolume;

    @NotNull
    private CarState carState;

    @PositiveOrZero
    private Integer mileage;

    @Positive
    private Integer power;

    @Size(min = 1, max = 3)
    private List<String> phoneNumbers;

    // todo add images
}
