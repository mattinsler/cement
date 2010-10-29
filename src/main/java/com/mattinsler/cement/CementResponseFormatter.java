package com.mattinsler.cement;

import com.google.inject.Inject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 19, 2010
 * Time: 10:42:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class CementResponseFormatter {
    private final Map<String, CementResponseWriter> _writers = new HashMap<String, CementResponseWriter>();

    @Inject
    public CementResponseFormatter(Set<CementResponseWriter> writers) {
        for (CementResponseWriter writer : writers) {
            _writers.put(writer.getFormat(), writer);
        }
    }

    public boolean canFormat(String format) {
        return _writers.containsKey(format);
    }

    // returns content type
    public String format(Object value, PrintWriter writer, String format) throws IOException {
        CementResponseWriter responseWriter = _writers.get(format);
        if (responseWriter == null) {
            throw new RuntimeException("Unsupported format: " + format);
        }
        return responseWriter.write(value, writer);
    }
}
