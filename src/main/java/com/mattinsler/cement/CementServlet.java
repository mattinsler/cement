package com.mattinsler.cement;

import com.google.inject.Injector;
import com.google.inject.Provider;
import com.mattinsler.cement.contract.CementErrorContract;
import com.mattinsler.cement.exception.CementException;
import com.mattinsler.cement.exception.CementUnexpectedException;
import com.mattinsler.cement.logging.CementLogger;
import com.mattinsler.cement.routing.CementExecutableMethod;
import com.mattinsler.cement.routing.CementMethodRouter;
import com.mattinsler.cement.routing.CementMethodType;
import com.mattinsler.contract.ContractSerializationService;
import com.mattinsler.contract.IsContract;
import com.mattinsler.contract.exception.UnknownFormatException;
import com.mattinsler.contract.exception.UnknownSerializerException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    private final ContractSerializationService _serializationService;
    private final String _defaultResponseFormat;

    private final Provider<CementRequestParameters> _requestParametersProvider;

    public CementServlet(Injector injector, CementMethodRouter router, Object handler, CementLogger logger, ContractSerializationService serializationService, String defaultResponseFormat, Provider<CementRequestParameters> requestParametersProvider) {
        _injector = injector;
        _router = router;
        _handler = handler;
        _logger = logger;
        _serializationService = serializationService;
        _defaultResponseFormat = defaultResponseFormat;
        _requestParametersProvider = requestParametersProvider;
    }

    private void execute(CementMethodType type, HttpServletRequest request, HttpServletResponse response) {
        execute(type, request, response, _requestParametersProvider.get());
    }

    private void execute(CementMethodType type, HttpServletRequest request, HttpServletResponse response, CementRequestParameters parameters) {
        String format = parameters.getAndRemove("format");
        CementExecutableMethod executableMethod = null;
        try {
            executableMethod = _router.route(type, request.getPathInfo(), parameters);

            if (format == null && executableMethod.hasResponseFormat()) {
                format = executableMethod.getResponseFormat();
            }

            Object result = executableMethod.execute(_handler);
            respond(response, executableMethod, result, format);
        } catch (CementException e) {
            _logger.error(e);
            respondException(response, executableMethod, e, format);
        } catch (Throwable t) {
            _logger.error("An unexpected error occurred in processing a request", t);
            t.printStackTrace();
            respondException(response, executableMethod, new CementUnexpectedException(t), format);
        }
    }

    private void respondException(HttpServletResponse response, CementExecutableMethod scope, CementException exception, String format) {
        Class<? extends IsContract> contract;
        if (scope != null && scope.getErrorContract() != null) {
            contract = scope.getErrorContract();
        } else {
            contract = CementErrorContract.class;
        }
        writeResponse(response, exception.getStatusCode(), exception, contract, format);
    }

    private void respond(HttpServletResponse response, CementExecutableMethod scope, Object responseValue, String format) {
        format = format == null ? _defaultResponseFormat : format;
        if (scope.hasFormatSerializer(format)) {
            writeResponse(response, HttpServletResponse.SC_OK, responseValue, scope.getFormatSerializer(format));
        } else {
            writeResponse(response, HttpServletResponse.SC_OK, responseValue, scope.getResponseContract(), format);
        }
    }

    private void writeError(HttpServletResponse response, UnknownFormatException e) {
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unknown format: " + e.getFormat());
        } catch (IOException f) {
            f.printStackTrace();
        }
    }

    private String formatClassName(Class<?> type) {
        if (type.getPackage() == null) {
            return type.getName();
        }
        return type.getName().substring(type.getPackage().getName().length() + 1);
    }

    private void writeError(HttpServletResponse response, UnknownSerializerException e) {
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unknown serializer: " + formatClassName(e.getValueType()) + " -> " + formatClassName(e.getContractType()));
        } catch (IOException f) {
            f.printStackTrace();
        }
    }

    private void writeError(HttpServletResponse response, RuntimeException e) {
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unknown exception: " + e.getMessage());
        } catch (IOException f) {
            f.printStackTrace();
        }
    }

    private void writeResponse(HttpServletResponse response, int status, Object responseValue, Class<? extends IsContract> responseContract, String format) {
        try {
            String contentType;
            if (responseContract == null) {
                contentType = _serializationService.serialize(response.getOutputStream(), responseValue, format);
            } else {
                contentType = _serializationService.serialize(response.getOutputStream(), responseContract, responseValue, format);
            }
            response.setStatus(status);
            response.setContentType(contentType);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnknownFormatException e) {
            writeError(response, e);
        } catch (UnknownSerializerException e) {
            writeError(response, e);
        } catch (RuntimeException e) {
            writeError(response, e);
            e.printStackTrace();
        }
    }

    private void writeResponse(HttpServletResponse response, int status, Object responseValue, CementFormatSerializer serializer) {
        try {
            String contentType = serializer.serialize(response.getOutputStream(), responseValue);
            response.setStatus(status);
            response.setContentType(contentType);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnknownFormatException e) {
            writeError(response, e);
        } catch (UnknownSerializerException e) {
            writeError(response, e);
        } catch (RuntimeException e) {
            writeError(response, e);
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
