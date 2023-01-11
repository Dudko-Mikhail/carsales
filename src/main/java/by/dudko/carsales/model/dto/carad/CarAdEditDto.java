package by.dudko.carsales.model.dto.carad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.Year;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarAdEditDto {
    @NotNull
    @PastOrPresent
    private Year year;

    @NotEmpty
    @Length(max = 128)
    private String brand;

    @NotEmpty
    @Length(max = 128)
    private String model;

    @Positive
    private Integer engineVolume;

    @PositiveOrZero
    private Integer mileage;

    @Positive
    private Integer power;
}
