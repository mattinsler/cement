package com.mattinsler.contract.exception;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/29/10
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnknownFormatException extends RuntimeException {
    private final String _format;

    public UnknownFormatException(String format) {
        super("Unknown format: " + format);
        _format = format;
    }

    public String getFormat() {
        return _format;
    }
}
