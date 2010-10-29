package com.mattinsler.cement.contract.json;

import com.mattinsler.cement.contract.DataContractSerializationWriter;
import com.mattinsler.cement.contract.ValueWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonSerializationWriter implements DataContractSerializationWriter {
    private static class Context {
        public String currentElement;
        public Object currentElementValue;
        public final StringBuilder builder = new StringBuilder(4096);
    }

    private final Stack<Context> _stack = new Stack<Context>();
    private final Map<Class<?>, ValueWriter<?>> _valueWriters = new HashMap<Class<?>, ValueWriter<?>>();

    private String _lastValue;

    public JsonSerializationWriter() {
        addValueWriter(StringBuilder.class, new ValueWriter<StringBuilder>() {
            public void write(Appendable appendable, StringBuilder value) throws IOException {
                appendable.append(value.toString());
            }
        });
        addValueWriter(java.sql.Date.class, (ValueWriter<java.sql.Date>)ValueWriters.QuotedValueWriter);
        addValueWriter(java.util.Date.class, (ValueWriter<java.util.Date>)ValueWriters.QuotedValueWriter);
        addValueWriter(Short.class, (ValueWriter<Short>)ValueWriters.UnquotedValueWriter);
        addValueWriter(Integer.class, (ValueWriter<Integer>)ValueWriters.UnquotedValueWriter);
        addValueWriter(Long.class, (ValueWriter<Long>)ValueWriters.UnquotedValueWriter);
    }

    private <T> void addValueWriter(Class<T> type, ValueWriter<T> writer) {
        _valueWriters.put(type, writer);
    }

    public void begin() {
        Context context = new Context();
        context.builder.append("{");
        _stack.push(context);
    }

    public void end() {
        Context context = _stack.pop();
        context.builder.append("}");
        if (!_stack.empty()) {
            _stack.peek().currentElementValue = context.builder;
        } else {
            _lastValue = context.builder.toString();
        }
    }

    public void beginElement(String name) {
        _stack.peek().currentElement = name;
    }

    public <T> void writeValue(T value) {
        _stack.peek().currentElementValue = value;
    }

    private <T> void appendValue(StringBuilder builder, T value) {
        ValueWriter<T> valueWriter = (ValueWriter<T>)_valueWriters.get(value.getClass());
        if (valueWriter == null) {
            valueWriter = (ValueWriter<T>)ValueWriters.QuotedValueWriter;
        }
        try {
            valueWriter.write(builder, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void endElement() {
        Context context = _stack.peek();
        if (context.currentElementValue != null) {
            if (context.builder.length() > 1) {
                context.builder.append(",");
            }

            context.builder.append("\"").append(context.currentElement).append("\"").append(":");
            appendValue(context.builder, context.currentElementValue);

            context.currentElementValue = null;
        }
    }

    @Override
    public String toString() {
        return _lastValue;
    }
}
