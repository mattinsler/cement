package com.mattinsler.cement;

import com.google.inject.Injector;
import com.mattinsler.cement.exception.CementException;
import com.mattinsler.cement.exception.CementUnexpectedException;
import com.mattinsler.cement.logging.CementLogger;
import com.mattinsler.cement.routing.CementExecutableMethod;
import com.mattinsler.cement.routing.CementMethodRouter;
import com.mattinsler.cement.routing.CementMethodType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 14, 2010
 * Time: 2:27:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class CementServlet extends HttpServlet {
    private final Injector _injector;
    private final Object _handler;
    private final CementLogger _logger;
    private final CementMethodRouter _router;
    private final CementResponseFormatter _responseFormatter;
    private final String _defaultResponseFormat;

    public CementServlet(Injector injector, CementMethodRouter router, Object handler, CementLogger logger, CementResponseFormatter responseFormatter, String defaultResponseFormat) {
        _injector = injector;
        _router = router;
        _handler = handler;
        _logger = logger;
        _responseFormatter = responseFormatter;
        _defaultResponseFormat = defaultResponseFormat;
    }

    private void execute(CementMethodType type, HttpServletRequest request, HttpServletResponse response) {
        String format = _defaultResponseFormat;
        try {
            // when path can't be found, throw 404
            CementRequestParameters parameters = _injector.getInstance(CementRequestParameters.class);
            CementExecutableMethod executableMethod = _router.route(type, request.getPathInfo(), parameters);

            if (executableMethod.format != null) {
                format = executableMethod.format;
            }

            Object result = executableMethod.execute(_handler);
            respond(response, HttpServletResponse.SC_OK, result, executableMethod.method.responseFormatter, format);
        } catch (CementException e) {
            _logger.error(e);
            respondException(response, e, format);
        } catch (Throwable t) {
            _logger.error("An unexpected error occurred in processing a request", t);
            t.printStackTrace();
            respondException(response, new CementUnexpectedException(t), format);
        }
    }

    private void respondException(HttpServletResponse response, CementException exception, String format) {
        respond(response, exception.getStatusCode(), exception, null, format);
    }

    private void respond(HttpServletResponse response, int statusCode, Object value, CementResponseFormatter methodFormatter, String format) {
        try {
            String contentType;
            if (value == null) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                if (methodFormatter != null && methodFormatter.canFormat(format)) {
                    contentType = methodFormatter.format(value, response.getWriter(), format);
                } else if (_responseFormatter.canFormat(format)) {
                    contentType = _responseFormatter.format(value, response.getWriter(), format);
                } else {
                    throw new RuntimeException("No Response Writer found for the specified format: " + format);
                }
                response.setContentType(contentType);
                response.setStatus(statusCode);
            }
        } catch (IOException e) {
            _logger.error("An unexpected error occurred while responding to a request", e);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(CementMethodType.Post, request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(CementMethodType.Get, request, response);
    }

    @Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(CementMethodType.Head, request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(CementMethodType.Put, request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(CementMethodType.Delete, request, response);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(CementMethodType.Options, request, response);
    }

    @Override
    protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(CementMethodType.Trace, request, response);
    }
}
