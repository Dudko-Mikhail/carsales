package by.dudko.carsales.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.regex.Matcher;

public abstract class RequestHandler {
    private final RequestInfo requestInfo;

    protected RequestHandler(RequestInfo requestInfo, RequestHandlerRegistry registry) {
        this.requestInfo = requestInfo;
        registry.addHandler(requestInfo, this);
    }

    public Matcher getPreparedMatcher(String uri) {
        Matcher matcher = requestInfo.matcher(uri);
        matcher.find();
        return matcher;
    }

    public abstract void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }
}
