package com.mattinsler.contract.formatter;

import com.mattinsler.contract.ValueFormatter;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/5/10
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractValueFormatter<ValueType> implements ValueFormatter<ValueType> {
    private final Class<? extends ValueType>[] _types;

    protected AbstractValueFormatter(Class<? extends ValueType> type) {
        _types = new Class[] { type };
    }

    protected AbstractValueFormatter(Class<? extends ValueType>... types) {
        _types = types;
    }

    public Class<? extends ValueType>[] getValueTypes() {
        return _types;
    }
}
