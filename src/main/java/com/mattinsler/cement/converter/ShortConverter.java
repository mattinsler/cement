package com.mattinsler.cement.converter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 18, 2010
 * Time: 2:11:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShortConverter implements Converter<Short> {
    public List<Class<?>> getSupportedTypes() {
        return Arrays.<Class<?>>asList(Short.class, short.class);
    }

    public Short fromString(String value) {
        return Short.valueOf(value);
    }

    public String toString(Short value) {
        return value.toString();
    }
}
