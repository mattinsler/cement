package com.mattinsler.contract;

import com.mattinsler.contract.exception.UnknownFieldFormatterException;
import com.mattinsler.contract.exception.UnknownSerializerException;
import com.mattinsler.contract.formatter.AbstractContractFormatter;
import com.mattinsler.contract.formatter.ContractFormatter;
import com.mattinsler.contract.formatter.ValueFormatter;
import com.mattinsler.contract.option.ContractOption;

import java.lang.reflect.Method;
import java.util.*;

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
        ValueFormatter<T> formatter = (ValueFormatter<T>)_formatters.get(type);

        if (formatter == null) {
            Stack<Class<?>> stack = new Stack<Class<?>>();
            stack.push(type);

            Class<?> newType = null;
            while (!stack.isEmpty()) {
                newType = stack.pop();
                formatter = (ValueFormatter<T>)_formatters.get(newType);
                if (formatter != null) {
                    _formatters.put(newType, formatter);
                    break;
                }
                if (newType.getSuperclass() != null) {
                    stack.add(newType.getSuperclass());
                }
                stack.addAll(Arrays.asList(newType.getInterfaces()));
            }
        }

        return formatter;
    }

    private static class ImplContractFormatter<C extends IsContract, T extends C> extends AbstractContractFormatter<C, C> {
        public ImplContractFormatter(Class<C> contract) {
            super(contract, contract);
        }

        @Override
        public void format(ContractSerializationWriter writer, C value, ValueMetadata metadata, ContractSerializationContext context) {
            writer.begin();

            for (Method method : getContractType().getDeclaredMethods()) {
                writer.beginElement(method.getName());
                try {
                    Object fieldValue = method.invoke(value);
                    if (fieldValue != null) {
                        context.formatValue(writer, fieldValue, new ValueMetadata());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                writer.endElement();
            }

            writer.end();
        }
    }

    public <T, C extends IsContract> ContractFormatter<T, C> getContractFormatter(Class<T> valueType, Class<C> contractType) {
        if (contractType.isAssignableFrom(valueType)) {
            return new ImplContractFormatter(contractType);
        }

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
        if (IsContract.class.isAssignableFrom(value.getClass())) {
            Class<?> type = value.getClass();
            Stack<Class<?>> stack = new Stack<Class<?>>();
            stack.push(type);
            while (!stack.isEmpty()) {
                type = stack.pop();
                if (Arrays.asList(type.getInterfaces()).contains(IsContract.class)) {
                    break;
                }
                stack.add(type.getSuperclass());
                stack.addAll(Arrays.asList(type.getInterfaces()));
            }

            new ImplContractFormatter(type).format(writer, (IsContract)value, new ValueMetadata(), this);
            return;
        }

        ContractOption contractOption = metadata.getOption(ContractOption.class);
        if (contractOption != null) {
            formatContract(writer, value, contractOption.getContractType());
        }
        ValueFormatter<T> formatter = (ValueFormatter<T>) getFormatter(value.getClass());
        if (formatter == null) {
            throw new UnknownFieldFormatterException(value.getClass());
        }
        formatter.format(writer, value, metadata, this);
    }
}
