package com.mattinsler.cement;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 26, 2010
 * Time: 9:02:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementTextToStringResponseWriter implements CementResponseWriter {
    @Override
    public String write(Object value, PrintWriter writer) throws IOException {
        writer.print(value);
        return "text/plain";
    }

    @Override
    public String getFormat() {
        return "text";
    }
}
