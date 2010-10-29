package com.mattinsler.cement.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mattinsler.cement.CementRequestParameters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/16/10
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementRequestParametersProvider implements Provider<CementRequestParameters> {
    private final Provider<HttpSession> _sessionProvider;
    private final Provider<HttpServletRequest> _requestProvider;

    @Inject
    CementRequestParametersProvider(Provider<HttpSession> sessionProvider, Provider<HttpServletRequest> requestProvider) {
        _sessionProvider = sessionProvider;
        _requestProvider = requestProvider;
    }

    public CementRequestParameters get() {
        CementRequestParameters parameters;
        parameters = (CementRequestParameters)_sessionProvider.get().getAttribute("parameters");
        if (parameters == null) {
            parameters = new CementRequestParameters(_requestProvider.get());
        }
        return parameters;
    }
}
