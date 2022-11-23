package by.dudko.carsales.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperHolder { // todo change after dependency injection
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return objectMapper;
    }

    private ObjectMapperHolder() {

    }
}
