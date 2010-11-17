package com.mattinsler.contract.exception;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/4/10
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnknownFieldFormatterException extends RuntimeException {
    private final Class<?> _valueType;

    public UnknownFieldFormatterException(Class<?> valueType) {
        super("Unknown field formatter: " + valueType.getSimpleName());
        _valueType = valueType;
    }

    public Class<?> getValueType() {
        return _valueType;
    }
}
