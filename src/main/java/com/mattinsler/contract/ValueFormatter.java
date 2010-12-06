package com.mattinsler.contract;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/4/10
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ValueFormatter<ValueType> {
    Class<? extends ValueType>[] getValueTypes();
    void format(ContractSerializationWriter writer, ValueType value, ValueMetadata metadata, ContractSerializationContext context);
}
