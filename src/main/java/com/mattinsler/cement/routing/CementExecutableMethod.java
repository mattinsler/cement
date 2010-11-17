package com.mattinsler.cement.routing;

import com.mattinsler.cement.exception.CementException;
import com.mattinsler.cement.exception.CementUnexpectedException;
import com.mattinsler.contract.IsContract;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 28, 2010
 * Time: 10:15:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementExecutableMethod {
    private final String _format;
    private final CementMethod _method;
    private final CementExecutableParameter[] _executableParameters;

    public CementExecutableMethod(CementMethod method, CementExecutableParameter[] executableParameters) {
        _method = method;
        _executableParameters = executableParameters;

        if (method.defaultResponseFormat != null && !"".equals(method.defaultResponseFormat)) {
            _format = method.defaultResponseFormat;
        } else {
            _format = null;
        }
    }

    public Object execute(Object handler) {
        // executableParameters can have an extra few blank entries ...
        Object[] arguments = new Object[_method.parameters.size() + _method.pathParameters.size() + _method.injectableParameters.size()];

        for (int x = 0; x < arguments.length; ++x) {
            arguments[x] = _executableParameters[x].get();
        }

        try {
            return _method.method.invoke(handler, arguments);
        } catch (IllegalAccessException e) {
            throw new CementUnexpectedException(e);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof CementException) {
                throw (CementException)t;
            } else {
                throw new CementUnexpectedException(e);
            }
        }
    }

    public boolean hasResponseFormat() {
        return _format != null;
    }

    public String getResponseFormat() {
        return _format;
    }

    public Class<? extends IsContract> getResponseContract() {
        return _method.responseContractType;
    }

    public Class<? extends IsContract> getErrorContract() {
        return _method.errorContractType;
    }
}
