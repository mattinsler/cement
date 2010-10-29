package com.mattinsler.cement.converter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 18, 2010
 * Time: 12:57:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class IntegerConverter implements Converter<Integer> {
    public List<Class<?>> getSupportedTypes() {
        return Arrays.<Class<?>>asList(Integer.class, int.class);
    }

    public String toString(Integer value) {
        return value.toString();
    }

    public Integer fromString(String value) {
        return Integer.valueOf(value);
    }
}
