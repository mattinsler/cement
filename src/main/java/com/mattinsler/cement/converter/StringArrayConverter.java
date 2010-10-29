package com.mattinsler.cement.converter;

import com.mattinsler.cement.util.StringUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 18, 2010
 * Time: 2:12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringArrayConverter implements Converter<String[]> {
    public List<Class<?>> getSupportedTypes() {
        return Arrays.<Class<?>>asList(String[].class);
    }

    public String[] fromString(String value) {
        return value.split(",");
    }

    public String toString(String[] value) {
        return StringUtil.join(value, ",");
    }
}
