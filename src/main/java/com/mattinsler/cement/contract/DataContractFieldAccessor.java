package com.mattinsler.cement.contract;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataContractFieldAccessor {
    String getName();
    Object getValue(Object object);
}
