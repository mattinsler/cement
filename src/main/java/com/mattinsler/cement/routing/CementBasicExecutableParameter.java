package com.mattinsler.cement.routing;

import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Oct 2, 2010
 * Time: 5:24:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementBasicExecutableParameter<T> implements CementExecutableParameter<T> {
    public final CementParameter<T> parameter;
    public final String value;

    public CementBasicExecutableParameter(CementParameter<T> parameter, String value) {
        this.parameter = parameter;
        this.value = value;
    }

    public T get() {
        try {
            return this.parameter.converter.fromString(URLDecoder.decode(this.value, "UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
