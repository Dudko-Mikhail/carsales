package by.dudko.carsales.model.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@ToString
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final String message;

    private final Map<String, Object> errors = new HashMap<>();

    public void addError(String description, Object error) {
        errors.put(description, error);
    }
}
