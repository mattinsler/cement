package com.mattinsler.contract.exception;

import com.mattinsler.contract.IsContract;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/3/10
 * Time: 11:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnknownSerializerException extends RuntimeException {
    private final Class<?> _valueType;
    private final Class<? extends IsContract> _contractType;

    public UnknownSerializerException(Class<?> valueType, Class<? extends IsContract> contractType) {
        super("Unknown serializer: " + valueType.getSimpleName() + " -> " + contractType.getSimpleName());
        _valueType = valueType;
        _contractType = contractType;
    }

    public Class<?> getValueType() {
        return _valueType;
    }

    public Class<? extends IsContract> getContractType() {
        return _contractType;
    }
}
