package com.mattinsler.cement.contract;

import com.google.inject.Inject;
import com.mattinsler.cement.IsDataContract;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataContractSerializationService {
    private final DataContractSerializationContext _context = new DataContractSerializationContext();
    private final Map<String, DataContractSerializationWriter> _writers = new HashMap<String, DataContractSerializationWriter>();

    private <ValueType, ContractType extends IsDataContract, MapperType extends DataContractMapper<ValueType, ContractType>> DataContractSerializer<ValueType, ContractType> createSerializer(Class<ValueType> valueType, Class<ContractType> contractType, Class<MapperType> mapperType) {
        try {
            MapperType mapper = (MapperType)mapperType.newInstance();
            mapper.createMapping(valueType, contractType);
            return new DataContractMapperSerializer<ValueType, ContractType>(mapper);
        } catch (Exception e) {
            return null;
        }
    }

    private <ValueType, ContractType extends IsDataContract, MapperType extends DataContractMapper<ValueType, ContractType>> void addSerializer(DataContractSerializationContext context, Class<ValueType> valueType, Class<ContractType> contractType, Class<MapperType> mapperType) {
        DataContractSerializer<ValueType, ContractType> serializer = createSerializer(valueType, contractType, mapperType);
        context.addSerializer(valueType, contractType, serializer);
    }

    @Inject
    DataContractSerializationService(Map<Class<? extends IsDataContract>, Class<? extends DataContractSerializer>> serializerTypes, Set<DataContractSerializationWriter> writers) {
        for (Map.Entry<Class<? extends IsDataContract>, Class<? extends DataContractSerializer>> entry : serializerTypes.entrySet()) {
            try {
                Class<?> valueType = null;
                Class<? extends IsDataContract> contractType = entry.getKey();

                // void serialize(DataContractSerializationWriter writer, ValueType value, Class<ContractType> contract, DataContractSerializationContext context);
                for (Method method : entry.getValue().getDeclaredMethods()) {
                    if ("serialize".equals(method.getName()) && method.getParameterTypes().length == 4) {
                        valueType = method.getParameterTypes()[1];
                    }
                }

                if (valueType == null) {
                    throw new RuntimeException("Could not determine value type for " + entry.getValue());
                }

                _context.addSerializer(valueType, contractType, entry.getValue().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (DataContractSerializationWriter writer : writers) {

        }
    }

    public <T, C extends IsDataContract> String serialize(T value, Class<C> contract) {
        _context.serialize(_writer, value, contract);
        return _writer.toString();
    }
}
