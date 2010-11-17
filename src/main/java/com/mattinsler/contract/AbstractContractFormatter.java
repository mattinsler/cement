package com.mattinsler.contract;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/29/10
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractContractFormatter<ValueType, ContractType extends IsContract> extends AbstractValueFormatter<ValueType> implements ContractFormatter<ValueType, ContractType> {
    private final Class<ContractType> _contractType;

    protected AbstractContractFormatter(Class<ValueType> valueType, Class<ContractType> contractType) {
        super(valueType);
        _contractType = contractType;
    }

    public Class<ContractType> getContractType() {
        return _contractType;
    }
}
