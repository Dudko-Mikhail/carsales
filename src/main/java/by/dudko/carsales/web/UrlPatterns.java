package by.dudko.carsales.web;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Getter
public class UrlPatterns {
    private final List<UrlPattern> urls = new ArrayList<>();
    private final UrlPattern single;
    private final UrlPattern all;
    private final String baseUrl;
    private final String idPattern;

    protected UrlPatterns(String baseUrl) {
        this(baseUrl, "\\d+");
    }

    protected UrlPatterns(String baseUrl, String idPattern) {
        this.baseUrl = baseUrl;
        this.idPattern = idPattern;
        single = () -> Pattern.compile(String.format("%s/(%s)", baseUrl, idPattern));
        all = () -> Pattern.compile(baseUrl);
        addAllPatternsToList();
    }

    protected void addAllPatternsToList() {
        urls.add(single);
        urls.add(all);
    }

    public void addUrlPattern(UrlPattern urlPattern) {
        if (urlPattern != null) {
            urls.add(urlPattern);
        }
    }

    public UrlPattern buildPatternWithBaseUrl(String pattern) {
        if (pattern == null) {
            return all;
        }
        return () -> Pattern.compile(String.format("%s/%s", baseUrl, pattern));
    }

    public Optional<UrlPattern> parseUrl(String url) {
        return urls.stream()
                .filter(pattern -> pattern.matches(url))
                .findFirst();
    }
}
