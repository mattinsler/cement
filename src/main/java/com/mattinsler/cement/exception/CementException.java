package com.mattinsler.cement.exception;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 14, 2010
 * Time: 9:19:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementException extends RuntimeException {
    private final int _statusCode;

    public CementException() {
        this(0);
    }

    public CementException(int statusCode) {
        _statusCode = statusCode;
    }

    public CementException(int statusCode, String message) {
        super(message);
        _statusCode = statusCode;
    }

    public CementException(int statusCode, String message, Throwable throwable) {
        super(message, throwable);
        _statusCode = statusCode;
    }

    public CementException(int statusCode, Throwable throwable) {
        super(throwable);
        _statusCode = statusCode;
    }

    public int getStatusCode() {
        return _statusCode;
    }
}
