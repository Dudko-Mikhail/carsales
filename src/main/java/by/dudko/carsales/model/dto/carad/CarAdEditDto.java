package by.dudko.carsales.model.dto.carad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.Year;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarAdEditDto {
    @PastOrPresent
    private Year year;

    @NotEmpty
    private String brand;

    @NotEmpty
    private String model;

    @Positive
    private Integer engineVolume;

    @PositiveOrZero
    private Integer mileage;
    
    @Positive
    private Integer power;
}
