package com.mattinsler.cement.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Oct 10, 2010
 * Time: 7:44:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementUnauthorizedException extends CementException {
    public CementUnauthorizedException() {
        super(HttpServletResponse.SC_UNAUTHORIZED);
    }

    public CementUnauthorizedException(String message) {
        super(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
