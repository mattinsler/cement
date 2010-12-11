package com.mattinsler.contract.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mattinsler.contract.ContractMapper;
import com.mattinsler.contract.formatter.ContractMapperFormatter;
import com.mattinsler.contract.IsContract;
import com.mattinsler.contract.MetadataService;
import com.mattinsler.contract.exception.InitializationException;
import net.sf.cglib.core.ReflectUtils;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/29/10
 * Time: 7:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContractMapperSerializerProvider implements Provider<ContractMapperFormatter> {
    private final Class<?> _valueType;
    private final Class<? extends IsContract> _contractType;
    private final Class<? extends ContractMapper> _mapperType;

    private MetadataService _metadataService;
    private ContractMapperFormatter _serializer;

    public ContractMapperSerializerProvider(Class<? extends ContractMapper> mapperType) {
        _mapperType = mapperType;

        Class<?> valueType = null;
        Class<? extends IsContract> contractType = null;
        for (Method method : _mapperType.getDeclaredMethods()) {
            if ("mapContract".equals(method.getName()) && void.class.equals(method.getReturnType()) && method.getParameterTypes().length == 2) {
                contractType = (Class<? extends IsContract>)method.getParameterTypes()[0];
                valueType = method.getParameterTypes()[1];
                break;
            }
        }
        if (valueType == null || contractType == null) {
            throw new InitializationException("Could not find value type and contract type from mapContract method");
        }
        _valueType = valueType;
        _contractType = contractType;
    }

    @Inject
    void injectDependencies(MetadataService metadataService) {
        _metadataService = metadataService;
    }

    public ContractMapperFormatter get() {
        if (_serializer == null) {
            try {
                ContractMapper mapper = (ContractMapper)ReflectUtils.newInstance(_mapperType);
                mapper.createMapping(_valueType, _contractType, _metadataService);
                _serializer = new ContractMapperFormatter(_valueType, _contractType, mapper);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return _serializer;
    }
}
