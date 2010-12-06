package com.mattinsler.contract;

import com.mattinsler.cement.util.Function;
import com.mattinsler.cement.util.StringUtil;
import com.mattinsler.contract.exception.InitializationException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/20/10
 * Time: 2:12 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ContractMapper<ValueType, ContractType extends IsContract> {
    private MetadataService _metadataService;

    private static class LastCalledMethodInterceptor implements MethodInterceptor {
        public Method lastMethod;

        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            lastMethod = method;
            return null;
        }
    }

    public interface ValueGetter {
        ValueMetadata getMetadata();
        Object get(Object value);
    }

    private abstract class AbstractValueGetter implements ValueGetter {
        private final ValueMetadata _metadata;
        protected AbstractValueGetter(Method contractMethod) {
            _metadata = _metadataService.createMetadata(contractMethod);
        }
        public ValueMetadata getMetadata() {
            return _metadata;
        }
    }

    private class MethodValueGetter extends AbstractValueGetter {
        private final Method _valueMethod;
        public MethodValueGetter(Method contractMethod, Method valueMethod) {
            super(contractMethod);
            _valueMethod = valueMethod;
        }
        public Object get(Object value) {
            try {
                return _valueMethod.invoke(value);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private class FieldMapperValueGetter<ValueType, FieldType> extends AbstractValueGetter {
        private final FieldMapper<ValueType, FieldType> _fieldMapper;
        public FieldMapperValueGetter(Method contractMethod, FieldMapper<ValueType, FieldType> fieldMapper) {
            super(contractMethod);
            _fieldMapper = fieldMapper;
        }
        public Object get(Object value) {
            return _fieldMapper.mapField((ValueType)value);
        }
    }

    private Map<Method, ValueGetter> _fieldToGetter = new HashMap<Method, ValueGetter>();
    private LastCalledMethodInterceptor _lastValueMethod = new LastCalledMethodInterceptor();
    private LastCalledMethodInterceptor _lastContractMethod = new LastCalledMethodInterceptor();

    public interface FieldMapper<ValueType, FieldType> {
        FieldType mapField(ValueType value);
    }

    public class Mapper<FieldType> {
        public <T> void to(T valueObjectMethod) {
            Method contractMethod = _lastContractMethod.lastMethod;
            Method valueMethod = _lastValueMethod.lastMethod;
            if (IsContract.class.isAssignableFrom(contractMethod.getReturnType())) {
//                _contractFieldToGetter.put(contractMethod, new MethodValueGetter(contractMethod, valueMethod));
                _fieldToGetter.put(contractMethod, new MethodValueGetter(contractMethod, valueMethod));
            } else {
                if (!contractMethod.getReturnType().equals(valueMethod.getReturnType())) {
                    throw new RuntimeException("The contract field and the value field must be the same types");
                }
                _fieldToGetter.put(contractMethod, new MethodValueGetter(contractMethod, valueMethod));
            }
        }
        public <T> void to(FieldMapper<ValueType, T> fieldMapper) {
            Method contractMethod = _lastContractMethod.lastMethod;
            _fieldToGetter.put(contractMethod, new FieldMapperValueGetter(contractMethod, fieldMapper));
        }
    }

    protected abstract void mapContract(ContractType contract, ValueType value);

    protected <FieldType> Mapper<FieldType> map(FieldType contractMethod) {
        return new Mapper<FieldType>();
    }

    public void createMapping(Class<ValueType> valueType, Class<ContractType> contractType, MetadataService metadataService) {
        _metadataService = metadataService;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(valueType);
        enhancer.setCallback(_lastValueMethod);
        ValueType value = (ValueType)enhancer.create();

        enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{contractType});
        enhancer.setCallback(_lastContractMethod);
        ContractType contract = (ContractType)enhancer.create();

        mapContract(contract, value);

        Set<Method> contractMethods = new HashSet<Method>(Arrays.asList(contractType.getDeclaredMethods()));
        contractMethods.removeAll(_fieldToGetter.keySet());

        if (contractMethods.size() > 0) {
            throw new InitializationException("No mapping declared for " + StringUtil.join(contractMethods, ", ", new Function<String, Method>() {
                public String execute(Method argument) {
                    return argument.getName();
                }
            }) + " in " + getClass());
        }
    }

    public Map<Method, ValueGetter> getMapping() {
        return _fieldToGetter;
    }
}
