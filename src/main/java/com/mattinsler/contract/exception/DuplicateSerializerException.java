package com.mattinsler.contract.exception;

import com.mattinsler.contract.IsContract;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/29/10
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class DuplicateSerializerException extends RuntimeException {
    private final Class<?> _valueType;
    private final Class<? extends IsContract> _contractType;

    public DuplicateSerializerException(Class<?> valueType, Class<? extends IsContract> contractType) {
        super("Duplicate serializer for " + valueType + " -> " + contractType);
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
