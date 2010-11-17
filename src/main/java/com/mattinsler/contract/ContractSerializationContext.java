package com.mattinsler.contract;

import com.mattinsler.contract.exception.UnknownFieldFormatterException;
import com.mattinsler.contract.exception.UnknownSerializerException;
import com.mattinsler.contract.option.ContractOption;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContractSerializationContext {
    private final Map<Class<?>, ValueFormatter<?>> _fieldFormatters = new HashMap<Class<?>, ValueFormatter<?>>();
    private final Map<Class<?>, Map<Class<? extends IsContract>, ContractFormatter>> _contractFormatters = new HashMap<Class<?>, Map<Class<? extends IsContract>, ContractFormatter>>();

    public <ValueType> void addFieldFormatter(ValueFormatter<ValueType> formatter) {
        for (Method method : formatter.getClass().getMethods()) {
            if ("format".equals(method.getName()) && method.getParameterTypes().length == 2 && ContractSerializationWriter.class.equals(method.getParameterTypes()[0])) {
                _fieldFormatters.put(method.getParameterTypes()[1], formatter);
                break;
            }
        }
    }

    public <ContractType extends IsContract, ValueType> void addSerializer(ContractFormatter<ContractType, ValueType> formatter) {
        addSerializer(formatter.getContractType(), formatter.getValueType(), formatter);
    }

    public <ContractType extends IsContract, ValueType> void addSerializer(Class<ContractType> contractType, Class<ValueType> valueType, ContractFormatter<ContractType, ValueType> formatter) {
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

    public ContractFormatter getSerializer(Class<? extends IsContract> contractType, Class<?> valueType) {
        Map<Class<? extends IsContract>, ContractFormatter> contracts = _serializers.get(valueType);

        boolean hasValueType = (contracts != null);
        if (!hasValueType) {
            Class<?> tempValueType = valueType;
            while (contracts == null && tempValueType != null) {
                tempValueType = tempValueType.getSuperclass();
                contracts = _serializers.get(tempValueType);
            }
            if (contracts == null) {
                return null;
            }
        }

        ContractFormatter formatter = contracts.get(contractType);
        if (!hasValueType) {
            addSerializer(contractType, valueType, formatter);
        }

        return formatter;
    }

    public <ContractType extends IsContract, ValueType> void formatContract(ContractSerializationWriter writer, Class<ContractType> contract, ValueType value) throws UnknownSerializerException {
        ContractFormatter<ContractType> formatter = getSerializer(contract, value.getClass());
        if (formatter == null) {
            throw new UnknownSerializerException(value.getClass(), contract);
        }
        formatter.format(writer, value, null, this);
    }

    public <ValueType> void formatValue(ContractSerializationWriter writer, ValueType value, ValueMetadata metadata) {
        ContractOption contractOption = metadata.getOption(ContractOption.class);
        if (contractOption != null) {
            serialize(writer, contractOption.getContractType(), value);
        }
        ValueFormatter<ValueType> formatter = (ValueFormatter<ValueType>)_fieldFormatters.get(value.getClass());
        if (formatter == null) {
            throw new UnknownFieldFormatterException(value.getClass());
        }
        formatter.format(writer, value, metadata, this);
    }
}
