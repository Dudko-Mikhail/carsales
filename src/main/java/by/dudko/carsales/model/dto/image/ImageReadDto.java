package by.dudko.carsales.model.dto.image;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ImageReadDto {
    long id;
    long adId;
    String imageName;
}
