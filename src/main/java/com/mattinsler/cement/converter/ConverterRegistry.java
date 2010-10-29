package com.mattinsler.cement.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 18, 2010
 * Time: 1:24:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConverterRegistry {
    private static ConverterRegistry _instance;
    public static synchronized ConverterRegistry get() {
        if (_instance == null) {
            _instance = new ConverterRegistry();
        }
        return _instance;
    }

    private Map<Class<?>, Converter<?>> _converters = new HashMap<Class<?>, Converter<?>>();

    private ConverterRegistry() {
        register(new IntegerConverter());
        register(new LongConverter());
        register(new StringConverter());
    }

    public <T> void register(Converter<T> converter) {
        for (Class<?> type : converter.getSupportedTypes()) {
            _converters.put(type, converter);
        }
    }

    public <T> Converter<T> getConverter(Class<?> type) {
        return (Converter<T>)_converters.get(type);
    }
}
