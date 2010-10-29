package com.mattinsler.cement;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 19, 2010
 * Time: 10:51:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CementResponseWriter {
    String write(Object value, PrintWriter writer) throws IOException;
    String getFormat();
}
