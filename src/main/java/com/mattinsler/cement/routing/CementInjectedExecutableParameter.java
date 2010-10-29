package com.mattinsler.cement.routing;

import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import com.mattinsler.cement.exception.CementException;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Oct 2, 2010
 * Time: 5:25:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementInjectedExecutableParameter<T> implements CementExecutableParameter<T> {
    public final CementParameter<T> parameter;
    private final Injector _injector;

    public CementInjectedExecutableParameter(CementParameter<T> parameter, Injector injector) {
        this.parameter = parameter;
        _injector = injector;
    }

    public T get() {
        try {
            return _injector.getInstance(parameter.bindingKey);
        } catch (ProvisionException e) {
            Throwable t = e.getCause();
            if (t instanceof CementException) {
                throw (CementException)t;
            } else {
                throw e;
            }
        }
    }
}
