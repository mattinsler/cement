package com.mattinsler.contract.exception;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/30/10
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class InitializationException extends RuntimeException {
    public InitializationException() {
        super();
    }

    public InitializationException(String message) {
        super(message);
    }
}
