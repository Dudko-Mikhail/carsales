package by.dudko.carsales.model.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypeMismatchError {
    String propertyName;
    Class<?> requiredType;
    Object rejectedValue;
    String message;
}
