package com.mattinsler.cement.converter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/9/10
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class EnumConverter<T extends Enum<T>> implements Converter<T> {
    private final Map<String, T> _map = new HashMap<String, T>();

    public EnumConverter(Class<T> enumType) throws Exception {
        for (Object value : (Object[])enumType.getMethod("values").invoke(null)) {
            _map.put(((String)value.getClass().getMethod("name").invoke(value)).toLowerCase(), (T)value);
        }
    }

    public List<Class<?>> getSupportedTypes() {
        return Arrays.<Class<?>>asList(String.class);
    }

    public T fromString(String value) {
        return _map.get(value.toLowerCase());
    }

    public String toString(T value) {
        return value.name();
    }
}
