package com.mattinsler.cement.converter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 18, 2010
 * Time: 12:53:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class StringConverter implements Converter<String> {
    public List<Class<?>> getSupportedTypes() {
        return Arrays.<Class<?>>asList(String.class);
    }

    public String fromString(String value) {
        return value;
    }

    public String toString(String value) {
        return value;
    }
}
