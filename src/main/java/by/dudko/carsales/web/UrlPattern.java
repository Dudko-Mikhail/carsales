package by.dudko.carsales.web;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FunctionalInterface
public interface UrlPattern {
    Pattern getPattern();

    default boolean matches(String url) {
        Predicate<String> matchPredicate = getPattern().asMatchPredicate();
        return matchPredicate.test(url);
    }

    default Matcher matcher(String url) {
        return getPattern().matcher(url);
    }
}
