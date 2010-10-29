package com.mattinsler.cement.routing;

import com.mattinsler.cement.exception.CementException;
import com.mattinsler.cement.exception.CementUnexpectedException;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 28, 2010
 * Time: 10:15:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementExecutableMethod {
    public final String format;
    public final CementMethod method;
    public final CementExecutableParameter[] executableParameters;

    public CementExecutableMethod(CementMethod method, CementExecutableParameter[] executableParameters, String format) {
        this.method = method;
        this.executableParameters = executableParameters;

        if (format != null && !"".equals(format)) {
            this.format = format;
        } else if (method.defaultResponseFormat != null && !"".equals(method.defaultResponseFormat)) {
            this.format = method.defaultResponseFormat;
        } else {
            this.format = null;
        }
    }

    public Object execute(Object handler) {
        // executableParameters can have an extra few blank entries ...
        Object[] arguments = new Object[this.method.parameters.size() + this.method.pathParameters.size() + this.method.injectableParameters.size()];

        for (int x = 0; x < arguments.length; ++x) {
            arguments[x] = this.executableParameters[x].get();
        }

        try {
            return this.method.method.invoke(handler, arguments);
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
}
