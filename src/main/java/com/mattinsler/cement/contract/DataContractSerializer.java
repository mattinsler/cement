package com.mattinsler.cement.contract;

import com.mattinsler.cement.IsDataContract;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataContractSerializer<ValueType, ContractType extends IsDataContract> {
    void serialize(DataContractSerializationWriter writer, ValueType value, Class<ContractType> contract, DataContractSerializationContext context);
}
