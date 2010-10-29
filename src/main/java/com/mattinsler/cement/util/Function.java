package com.mattinsler.cement.util;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 18, 2010
 * Time: 2:23:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Function<ReturnType, ArgumentType> {
    ReturnType execute(ArgumentType argument);
}
