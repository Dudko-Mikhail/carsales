package by.dudko.carsales.model.dto.error;

import lombok.Builder;
import lombok.Value;
import org.springframework.validation.ObjectError;

@Value
@Builder
public class GlobalError {
    String objectName;
    String message;

    public static GlobalError of(ObjectError error) {
        return GlobalError.builder()
                .objectName(error.getObjectName())
                .message(error.getDefaultMessage())
                .build();
    }
}