package com.mattinsler.contract.exception;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/29/10
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class DuplicateWriterException extends RuntimeException {
    private final String _type;

    public DuplicateWriterException(String type) {
        super("Duplicate writer for type " + type);
        _type = type;
    }

    public String getType() {
        return _type;
    }
}
