package com.mattinsler.cement.contract;

import com.mattinsler.cement.IsDataContract;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/20/10
 * Time: 2:12 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class DataContractMapper<ValueType, ContractType extends IsDataContract> {
    private static class LastCalledMethodInterceptor implements MethodInterceptor {
        public Method lastMethod;

        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            lastMethod = method;
            return null;
        }
    }

    public interface ValueGetter {
        Object get(Object value);
    }

    private static class MethodValueGetter implements ValueGetter {
        private final Method _method;
        public MethodValueGetter(Method method) {
            _method = method;
        }
        public Object get(Object value) {
            try {
                return _method.invoke(value);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private static class FieldMapperValueGetter<ValueType, FieldType> implements ValueGetter {
        private final FieldMapper<ValueType, FieldType> _fieldMapper;
        public FieldMapperValueGetter(FieldMapper<ValueType, FieldType> fieldMapper) {
            _fieldMapper = fieldMapper;
        }
        public Object get(Object value) {
            return _fieldMapper.mapField((ValueType)value);
        }
    }

    private Map<Method, ValueGetter> _fieldToGetter = new HashMap<Method, ValueGetter>();
    private Map<Method, ValueGetter> _contractFieldToGetter = new HashMap<Method, ValueGetter>();
    private LastCalledMethodInterceptor _lastValueMethod = new LastCalledMethodInterceptor();
    private LastCalledMethodInterceptor _lastContractMethod = new LastCalledMethodInterceptor();

    public interface FieldMapper<ValueType, FieldType> {
        FieldType mapField(ValueType value);
    }

    public class Mapper<FieldType> {
        public <T> void to(T valueMethod) {
            if (IsDataContract.class.isAssignableFrom(_lastContractMethod.lastMethod.getReturnType())) {
                _contractFieldToGetter.put(_lastContractMethod.lastMethod, new MethodValueGetter(_lastValueMethod.lastMethod));
            } else {
                if (!_lastContractMethod.lastMethod.getReturnType().equals(_lastValueMethod.lastMethod.getReturnType())) {
                    throw new RuntimeException("The contract field and the value field must be the same types");
                }
                _fieldToGetter.put(_lastContractMethod.lastMethod, new MethodValueGetter(_lastValueMethod.lastMethod));
            }
        }
        public void to(FieldMapper<ValueType, FieldType> fieldMapper) {
            _fieldToGetter.put(_lastContractMethod.lastMethod, new FieldMapperValueGetter(fieldMapper));
        }
    }

    protected abstract void mapContract(ValueType value, ContractType contract);

    protected <FieldType> Mapper<FieldType> map(FieldType contractMethod) {
        return new Mapper<FieldType>();
    }

    public void createMapping(Class<ValueType> valueType, Class<ContractType> contractType) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(valueType);
        enhancer.setCallback(_lastValueMethod);
        ValueType value = (ValueType) enhancer.create();

        enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{contractType});
        enhancer.setCallback(_lastContractMethod);
        ContractType contract = (ContractType)enhancer.create();

        mapContract(value, contract);
    }

    public Map<Method, ValueGetter> getMapping() {
        return _fieldToGetter;
    }

    public Map<Method, ValueGetter> getContractMapping() {
        return _contractFieldToGetter;
    }
}
