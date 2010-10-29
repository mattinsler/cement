package com.mattinsler.cement.contract;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ValueWriter<T> {
    void write(Appendable appendable, T value) throws IOException;
}
