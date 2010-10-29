package com.mattinsler.cement.contract.json;

import com.mattinsler.cement.contract.ValueWriter;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ValueWriters {
    public static final ValueWriter<?> QuotedValueWriter = new ValueWriter<Object>() {
        public void write(Appendable appendable, Object value) throws IOException {
            appendable.append("\"").append(value.toString()).append("\"");
        }
    };

    public static final ValueWriter<?> UnquotedValueWriter = new ValueWriter<Object>() {
        public void write(Appendable appendable, Object value) throws IOException {
            appendable.append(value.toString());
        }
    };
}
