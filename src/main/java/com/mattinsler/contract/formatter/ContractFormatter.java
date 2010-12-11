package com.mattinsler.contract.formatter;

import com.mattinsler.contract.IsContract;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ContractFormatter<ValueType, ContractType extends IsContract> extends ValueFormatter<ValueType> {
    Class<ContractType> getContractType();
}
