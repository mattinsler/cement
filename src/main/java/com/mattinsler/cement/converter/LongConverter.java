package com.mattinsler.cement.converter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 18, 2010
 * Time: 1:00:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class LongConverter implements Converter<Long> {
    public List<Class<?>> getSupportedTypes() {
        return Arrays.<Class<?>>asList(Long.class, long.class);
    }

    public Long fromString(String value) {
        return Long.valueOf(value);
    }

    public String toString(Long value) {
        return value.toString();
    }
}
