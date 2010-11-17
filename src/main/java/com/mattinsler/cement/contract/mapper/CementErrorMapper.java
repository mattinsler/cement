package com.mattinsler.cement.contract.mapper;

import com.mattinsler.cement.contract.CementErrorContract;
import com.mattinsler.cement.exception.CementException;
import com.mattinsler.contract.ContractMapper;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/4/10
 * Time: 12:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class CementErrorMapper extends ContractMapper<CementErrorContract, CementException> {
    protected void mapContract(CementErrorContract contract, CementException value) {
        map(contract.statusCode()).to(value.getStatusCode());
        map(contract.message()).to(value.getMessage());
    }
}
