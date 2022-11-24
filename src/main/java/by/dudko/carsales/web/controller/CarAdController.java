package by.dudko.carsales.web.controller;

import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.service.AdService;
import by.dudko.carsales.service.impl.AdServiceImpl;
import by.dudko.carsales.util.ObjectMapperHolder;
import by.dudko.carsales.web.RequestHandler;
import by.dudko.carsales.web.RequestHandlerRegistry;
import by.dudko.carsales.web.RequestInfo;
import by.dudko.carsales.web.UrlPattern;
import by.dudko.carsales.web.UrlPatterns;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

import java.io.IOException;
import java.util.Optional;

import static by.dudko.carsales.web.HttpMethod.DELETE;
import static by.dudko.carsales.web.HttpMethod.GET;
import static by.dudko.carsales.web.controller.CarAdController.CarAdUrlPatterns;

@WebServlet(urlPatterns = "/ads/*")
public class CarAdController extends BaseController<CarAdUrlPatterns> {
    private final ObjectMapper objectMapper = ObjectMapperHolder.getMapper();
    private final AdService adService = AdServiceImpl.getInstance();
    private final RequestHandlers requestHandlers = new RequestHandlers();

    public CarAdController() {
        super(new RequestHandlerRegistry());
    }

    @Override
    protected CarAdUrlPatterns getUrlPatterns() {
        return new CarAdUrlPatterns();
    }

    @Getter
    static class CarAdUrlPatterns extends UrlPatterns {
        private final UrlPattern fullInfoForAllAds = buildPatternWithBaseUrl("/full");
        private final UrlPattern fullInfoForSingleAd = buildPatternWithBaseUrl(getIdPattern() + "/full");

        public CarAdUrlPatterns() {
            super("/ads");
        }

        @Override
        protected void addAllPatternsToList() {
            super.addAllPatternsToList();
            addUrlPattern(fullInfoForAllAds);
            addUrlPattern(fullInfoForSingleAd);
        }
    }

    class RequestHandlers {
        private final RequestHandler findById = new RequestHandler(new RequestInfo(urlPatterns.getSingle(), GET), requestHandlerRegistry) {
            @Override
            public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
                long id = Long.parseLong(getPreparedMatcher(request.getRequestURI()).group(1));
                Optional<CarAd> ad = adService.findById(id);
                if (ad.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                // todo mapping to json
//                String json = objectMapper.writeValueAsString(ad.get());
//                WebUtils.writeBody(json, response);
            }
        };

        private final RequestHandler deleteById = new RequestHandler(new RequestInfo(urlPatterns.getSingle(), DELETE),
                requestHandlerRegistry) {
            @Override
            public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
                long id = Long.parseLong(getPreparedMatcher(request.getRequestURI()).group(1));
                int status = adService.deleteById(id) ? HttpServletResponse.SC_ACCEPTED : HttpServletResponse.SC_NOT_FOUND;
                response.sendError(status);
            }
        };
    }
}


