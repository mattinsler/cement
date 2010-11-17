package com.mattinsler.contract;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/5/10
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractValueFormatter<ValueType> implements ValueFormatter<ValueType> {
    private final Class<ValueType> _valueType;

    protected AbstractValueFormatter(Class<ValueType> valueType) {
        _valueType = valueType;
    }

    public Class<ValueType> getValueType() {
        return _valueType;
    }
}
