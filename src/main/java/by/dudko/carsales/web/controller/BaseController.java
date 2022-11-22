package by.dudko.carsales.web.controller;

import by.dudko.carsales.web.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public abstract class BaseController<U extends UrlPatterns> extends HttpServlet {
    protected final RequestHandlerRegistry requestHandlerRegistry;
    protected final U urlPatterns;

    protected BaseController(RequestHandlerRegistry requestHandlerRegistry) {
        this.requestHandlerRegistry = requestHandlerRegistry;
        this.urlPatterns = getUrlPatterns();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();
        if (method.equalsIgnoreCase(HttpMethod.PATCH.name())) {
            doPatch(request, response);
            return;
        }
        super.service(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response, HttpMethod.GET);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response, HttpMethod.POST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response, HttpMethod.PUT);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response, HttpMethod.DELETE);
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response, HttpMethod.PATCH);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, HttpMethod method) throws ServletException, IOException {
        String uri = request.getRequestURI();
        Optional<UrlPattern> urlPattern = urlPatterns.parseUrl(uri);
        if (urlPattern.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Optional<RequestHandler> handler = defineHandler(new RequestInfo(urlPattern.get(), method));
        if (handler.isEmpty()) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // fixme Какой код бросать: урл есть, но нет вызванный метод не поддерживается?
            return;
        }
        handler.get().handleRequest(request, response);
    }

    protected abstract U getUrlPatterns();

    protected Optional<RequestHandler> defineHandler(RequestInfo requestInfo) {
        return requestHandlerRegistry.defineHandler(requestInfo);
    }
}
