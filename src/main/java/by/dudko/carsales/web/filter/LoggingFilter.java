package by.dudko.carsales.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class LoggingFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Request method: {}, URI: {}, time: {}", request.getMethod(), request.getRequestURI(),
                System.currentTimeMillis());
        chain.doFilter(request, response);
        log.info("Response status: {}, time: {}", response.getStatus(), System.currentTimeMillis());
    }
}
