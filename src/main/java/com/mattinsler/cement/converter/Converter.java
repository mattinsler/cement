package com.mattinsler.cement.converter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 18, 2010
 * Time: 12:55:14 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Converter<T> {
    List<Class<?>> getSupportedTypes();

    T fromString(String value);
    String toString(T value);
}
