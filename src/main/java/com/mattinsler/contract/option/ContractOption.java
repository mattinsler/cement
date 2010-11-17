package com.mattinsler.contract.option;

import com.mattinsler.cement.annotation.Contract;
import com.mattinsler.contract.IsContract;
import com.mattinsler.contract.ValueMetadata;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/5/10
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContractOption implements ValueMetadata.Option {
    private final Class<? extends IsContract> _contractType;

    public ContractOption(Class<? extends IsContract> contractType) {
        _contractType = contractType;
    }

    public void recognize(Method method, ValueMetadata metadata) {
        Contract annotation = method.getAnnotation(Contract.class);
        if (annotation != null) {
            metadata.putOption(new ContractOption(annotation.value()));
        }
    }

    public Class<? extends IsContract> getContractType() {
        return _contractType;
    }
}
