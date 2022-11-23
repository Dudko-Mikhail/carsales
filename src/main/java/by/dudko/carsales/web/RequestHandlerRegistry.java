package by.dudko.carsales.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class RequestHandlerRegistry {
    private final Map<RequestInfo, RequestHandler> requestHandlers = new HashMap<>();

    public Optional<RequestHandler> defineHandler(RequestInfo requestInfo) {
        return Optional.ofNullable(requestHandlers.get(requestInfo));
    }

    public void addHandler(RequestInfo requestInfo, RequestHandler requestHandler) {
        requestHandlers.put(requestInfo, requestHandler);
    }
}
