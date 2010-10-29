package com.mattinsler.cement.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Oct 3, 2010
 * Time: 3:55:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementUnexpectedException extends CementException {
    public CementUnexpectedException(Throwable throwable) {
        super(HttpServletResponse.SC_OK, throwable);
    }
}
