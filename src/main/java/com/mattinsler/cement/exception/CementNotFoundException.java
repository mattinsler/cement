package com.mattinsler.cement.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Oct 3, 2010
 * Time: 3:43:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementNotFoundException extends CementException {
    public CementNotFoundException() {
        super(HttpServletResponse.SC_NOT_FOUND, "These are not the droids you're looking for");
    }
}
