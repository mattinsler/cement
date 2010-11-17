package com.mattinsler.contract;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/29/10
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContractMapperFormatter<ValueType, ContractType extends IsContract> extends AbstractContractFormatter<ValueType, ContractType> {
    private final ContractMapper<ValueType, ContractType> _mapper;

    public ContractMapperFormatter(Class<ValueType> valueType, Class<ContractType> contractType, ContractMapper<ValueType, ContractType> mapper) {
        super(valueType, contractType);
        _mapper = mapper;
    }

    public void format(ContractSerializationWriter writer, ValueType value, ValueMetadata metadata, ContractSerializationContext context) {
        writer.begin();

        for (Map.Entry<Method, ContractMapper.ValueGetter> e : _mapper.getMapping().entrySet()) {
            Object fieldValue = e.getValue().get(value);
            if (fieldValue != null) {
                writer.beginElement(e.getKey().getName());

                context.formatValue(writer, fieldValue, null);

//                if (Map.class.isAssignableFrom(e.getKey().getReturnType())) {
//                    Map<?, ?> map = (Map<?, ?>) fieldValue;
//                    if (map.size() > 0) {
//                        writer.begin();
//                        for (Map.Entry<?, ?> item : map.entrySet()) {
//                            writer.beginElement(item.getKey().toString());
//                            writer.writeValue(item.getValue());
//                            writer.endElement();
//                        }
//                        writer.end();
//                    }
//                } else {
                    writer.writeValue(fieldValue);
//                }

                writer.endElement();
            }
        }

        writer.end();
    }

    public void serialize(ContractSerializationWriter writer, Class<ContractType> contract, ValueType value, ContractSerializationContext context) {
        writer.begin();
        for (Map.Entry<Method, ContractMapper.ValueGetter> e : _mapper.getMapping().entrySet()) {
            Object fieldValue = e.getValue().get(value);
            if (fieldValue != null) {
                writer.beginElement(e.getKey().getName());

                if (Map.class.isAssignableFrom(e.getKey().getReturnType())) {
                    Map<?, ?> map = (Map<?, ?>) fieldValue;
                    if (map.size() > 0) {
                        writer.begin();
                        for (Map.Entry<?, ?> item : map.entrySet()) {
                            writer.beginElement(item.getKey().toString());
                            writer.writeValue(item.getValue());
                            writer.endElement();
                        }
                        writer.end();
                    }
                } else {
                    writer.writeValue(fieldValue);
                }

                writer.endElement();
            }
        }
        for (Map.Entry<Method, ContractMapper.ValueGetter> e : _mapper.getContractMapping().entrySet()) {
            Object contractFieldValue = e.getValue().get(value);
            if (contractFieldValue != null) {
                writer.beginElement(e.getKey().getName());
                context.serialize(writer, (Class<? extends IsContract>)e.getKey().getReturnType(), contractFieldValue);
                writer.endElement();
            }
        }
        writer.end();
    }
}
