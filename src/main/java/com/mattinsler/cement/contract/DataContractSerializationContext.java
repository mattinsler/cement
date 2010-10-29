package com.mattinsler.cement.contract;

import com.mattinsler.cement.IsDataContract;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataContractSerializationContext {
    private final Map<Class<?>, Map<Class<? extends IsDataContract>, DataContractSerializer>> _serializers = new HashMap<Class<?>, Map<Class<? extends IsDataContract>, DataContractSerializer>>();

    public <ValueType, ContractType extends IsDataContract> void addSerializer(Class<ValueType> valueType, Class<ContractType> contract, DataContractSerializer<? super ValueType, ContractType> serializer) {
        Map<Class<? extends IsDataContract>, DataContractSerializer> contracts = _serializers.get(valueType);
        if (contracts == null) {
            contracts = new HashMap<Class<? extends IsDataContract>, DataContractSerializer>();
            _serializers.put(valueType, contracts);
        }
        contracts.put(contract, serializer);
    }

    public DataContractSerializer getSerializer(Class<?> valueType, Class<? extends IsDataContract> contractType) {
        Map<Class<? extends IsDataContract>, DataContractSerializer> contracts = _serializers.get(valueType);

        boolean hasValueType = (contracts != null);
        if (!hasValueType) {
            Class<?> tempValueType = valueType;
            while (contracts == null) {
                tempValueType = tempValueType.getSuperclass();
                contracts = _serializers.get(tempValueType);
            }
            if (contracts == null) {
                return null;
            }
        }

        DataContractSerializer serializer = contracts.get(contractType);
        if (!hasValueType) {
            addSerializer(valueType, contractType, serializer);
        }

        return serializer;
//        if (serializer == null) {
//            Set<String> contractFields = new HashSet<String>(CollectionUtil.collect(contractType.getDeclaredFields(), new Function<String, Field>() {
//                public String execute(Field argument) {
//                    return argument.getName();
//                }
//            }));
//
//            Map<String, DataContractFieldAccessor> accessors = new HashMap<String, DataContractFieldAccessor>();
//            for (DataContractFieldAccessorFactory factory : _fieldAccessorFactories) {
//                for (DataContractFieldAccessor accessor : factory.createAccessors(valueType, contractType)) {
//                    accessors.put(accessor.getName(), accessor);
//                }
//            }
//            contractFields.removeAll(accessors.keySet());
//
//            if (contractFields.size() > 0) {
//                throw new RuntimeException("Could not find accessors for " + StringUtil.join(contractFields, ", "));
//            }
//
//            serializer = new DataContractSerializer(new ArrayList<DataContractFieldAccessor>(accessors.values()));
//            contracts.put(contractType, serializer);
//        }
    }

    public <ValueType, ContractType extends IsDataContract> void serialize(DataContractSerializationWriter writer, ValueType value, Class<ContractType> contract) {
        DataContractSerializer<ValueType, ContractType> serializer = getSerializer(value.getClass(), contract);
        if (serializer == null) {
            throw new RuntimeException("Could not find a valid serializer for " + value.getClass() + " -> " + contract);
        }
        serializer.serialize(writer, value, contract, this);
    }
}
