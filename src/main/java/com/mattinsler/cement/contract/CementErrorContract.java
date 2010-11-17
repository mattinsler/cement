package com.mattinsler.cement.contract;

import com.mattinsler.contract.IsContract;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/16/10
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CementErrorContract extends IsContract {
    int statusCode();
    String message();
}
