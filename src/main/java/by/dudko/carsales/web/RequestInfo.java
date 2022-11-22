package by.dudko.carsales.web;

import lombok.Value;

import java.util.regex.Matcher;

@Value
public class RequestInfo {
    UrlPattern urlPattern;
    HttpMethod httpMethod;

    public Matcher matcher(String url) {
        return urlPattern.matcher(url);
    }
}
