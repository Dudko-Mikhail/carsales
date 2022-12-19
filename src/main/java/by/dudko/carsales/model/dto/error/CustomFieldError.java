package by.dudko.carsales.model.dto.error;

import lombok.Builder;
import lombok.Value;
import org.springframework.validation.FieldError;

@Value
@Builder
public class CustomFieldError {
    String field;
    String message;
    Object rejectedValue;

    public static CustomFieldError of(FieldError error) {
        return CustomFieldError.builder()
                .field(error.getField())
                .message(error.getDefaultMessage())
                .rejectedValue(error.getRejectedValue())
                .build();
    }
}