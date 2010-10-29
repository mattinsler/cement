package com.mattinsler.cement.contract;

import com.mattinsler.cement.IsDataContract;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 8:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataContractFieldAccessorFactory {
    List<DataContractFieldAccessor> createAccessors(Class<?> valueType, Class<? extends IsDataContract> contractType, DataContractSerializationContext context);
}
