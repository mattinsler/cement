package com.mattinsler.contract.json;

import com.mattinsler.contract.ContractSerializationWriter;
import com.mattinsler.contract.ValueWriter;

import java.io.IOException;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonSerializationWriter implements ContractSerializationWriter {
    private static class Context {
        public boolean shouldAddComma;
        public String currentElementName;
        public Object currentElementValue;
    }

    private final Stack<Context> _stack = new Stack<Context>();

    private Appendable _output;

    public String getFormat() {
        return "json";
    }

    public String getContentType() {
        return "text/plain";
    }

    public void setOutput(Appendable appendable) {
        _output = appendable;
    }

    private void _begin(String openingLiteral) {
        Context context = new Context();
        Context parentContext = _stack.isEmpty() ? null : _stack.peek();
        try {
            if (parentContext != null) {
                if (parentContext.shouldAddComma) {
                    _output.append(',');
                }
                if (parentContext.currentElementName != null) {
                    _output.append("\"").append(parentContext.currentElementName).append("\"").append(":");
                }
            }
            _output.append(openingLiteral);
        } catch (IOException e) {
        }
        _stack.push(context);
    }

    private void _end(String closingLiteral) {
        _stack.pop();
        try {
            _output.append(closingLiteral);
        } catch (IOException e) {
        }
        if (!_stack.isEmpty()) {
            Context context = _stack.peek();
            context.shouldAddComma = true;
            context.currentElementValue = null;
        }
    }

    public void begin() {
        _begin("{");
    }

    public void end() {
        _end("}");
    }

    public void beginList() {
        _begin("[");
    }

    public void endList() {
        _end("]");
    }

    public void beginElement(String name) {
        _stack.peek().currentElementName = name;
    }

    public <T> void writeValue(T value) {
        _stack.peek().currentElementValue = value;
    }

    private <T> void appendValue(T value) throws IOException {
        ValueWriter<T> valueWriter = (ValueWriter<T>)_valueWriters.get(value.getClass());
        if (valueWriter == null) {
            valueWriter = (ValueWriter<T>)ValueWriters.QuotedValueWriter;
        }
        valueWriter.write(_output, value);
    }

    public void endElement() {
        Context context = _stack.peek();
        if (context.currentElementValue != null) {
            try {
                if (context.shouldAddComma) {
                    _output.append(",");
                }

                _output.append("\"").append(context.currentElementName).append("\"").append(":");
                appendValue(context.currentElementValue);
            } catch (IOException e) {
            }

            context.shouldAddComma = true;
            context.currentElementValue = null;
        }
    }
}
