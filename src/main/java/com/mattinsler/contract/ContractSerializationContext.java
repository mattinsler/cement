package com.mattinsler.contract;

import com.mattinsler.contract.exception.UnknownFieldFormatterException;
import com.mattinsler.contract.exception.UnknownSerializerException;
import com.mattinsler.contract.option.ContractOption;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContractSerializationContext {
    private final Map<Class<?>, ValueFormatter<?>> _formatters = new HashMap<Class<?>, ValueFormatter<?>>();
    private final Map<Class<?>, Map<Class<? extends IsContract>, ContractFormatter>> _contractFormatters = new HashMap<Class<?>, Map<Class<? extends IsContract>, ContractFormatter>>();

    public <ValueType> void addFormatter(ValueFormatter<ValueType> formatter) {
        for (Class<?> valueType : formatter.getValueTypes()) {
            _formatters.put(valueType, formatter);
        }
    }

    public <T, C extends IsContract> void addContractFormatter(ContractFormatter<T, C> formatter) {
        for (Class<?> valueType : formatter.getValueTypes()) {
            addContractFormatter((Class<T>)valueType, formatter.getContractType(), formatter);
        }
    }

    public <T, C extends IsContract> void addContractFormatter(Class<T> valueType, Class<C> contractType, ContractFormatter<T, C> formatter) {
        Map<Class<? extends IsContract>, ContractFormatter> contracts = _contractFormatters.get(valueType);
        if (contracts == null) {
            contracts = new HashMap<Class<? extends IsContract>, ContractFormatter>();
            _contractFormatters.put(valueType, contracts);
        }
        if (!contracts.containsKey(contractType)) {
            contracts.put(contractType, formatter);
        }
//        else {
//            throw new DuplicateSerializerException(valueType, contractType);
//        }
    }

    public <T> ValueFormatter<T> getFormatter(Class<T> type) {
        return (ValueFormatter<T>)_formatters.get(type);
    }

    public <T, C extends IsContract> ContractFormatter<T, C> getContractFormatter(Class<T> valueType, Class<C> contractType) {
        Map<Class<? extends IsContract>, ContractFormatter> contracts = _contractFormatters.get(valueType);

        boolean hasValueType = (contracts != null);
        if (!hasValueType) {
            Class<?> tempValueType = valueType;
            while (contracts == null && tempValueType != null) {
                tempValueType = tempValueType.getSuperclass();
                contracts = _contractFormatters.get(tempValueType);
            }
            if (contracts == null) {
                return null;
            }
        }

        ContractFormatter formatter = contracts.get(contractType);
        if (!hasValueType) {
            addContractFormatter(valueType, contractType, formatter);
        }

        return formatter;
    }

    public <T, C extends IsContract> void formatContract(ContractSerializationWriter writer, T value, Class<C> contract) throws UnknownSerializerException {
        formatContract(writer, (Class<T>)value.getClass(), contract, value);
    }

    public <T, C extends IsContract> void formatContract(ContractSerializationWriter writer, Class<T> valueType, Class<C> contract, T value) throws UnknownSerializerException {
        ContractFormatter<T, C> formatter = getContractFormatter(valueType, contract);
        if (formatter == null) {
            throw new UnknownSerializerException(valueType, contract);
        }
        formatter.format(writer, value, null, this);
    }

    public <T> void formatValue(ContractSerializationWriter writer, T value, ValueMetadata metadata) {
        ContractOption contractOption = metadata.getOption(ContractOption.class);
        if (contractOption != null) {
            formatContract(writer, value, contractOption.getContractType());
        }
        ValueFormatter<T> formatter = (ValueFormatter<T>) _formatters.get(value.getClass());
        if (formatter == null) {
            throw new UnknownFieldFormatterException(value.getClass());
        }
        formatter.format(writer, value, metadata, this);
    }
}
