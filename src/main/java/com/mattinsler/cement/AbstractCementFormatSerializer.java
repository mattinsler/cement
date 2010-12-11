package com.mattinsler.cement;

import com.mattinsler.contract.ContractSerializationWriter;
import com.mattinsler.contract.ValueMetadata;
import com.mattinsler.contract.exception.UnknownFormatException;
import com.mattinsler.contract.exception.UnknownSerializerException;

import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/10/10
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCementFormatSerializer implements CementFormatSerializer {
    private final String _format;

    private Appendable _appendable;
    private OutputStream _outputStream;

    protected AbstractCementFormatSerializer(String format) {
        _format = format;
    }

    protected Appendable getAppendable() {
        return _appendable;
    }

    protected OutputStream getOutputStream() {
        return _outputStream;
    }

    public String getFormat() {
        return _format;
    }

    protected abstract <T> String serialize(T value);

    public <T> String serialize(Appendable appendable, T value) {
        _appendable = appendable;

        String contentType = serialize(value);

        if (appendable instanceof Flushable) {
            try {
                ((Flushable)appendable).flush();
            } catch (IOException e) {
            }
        }
        return contentType;
    }

    public <T> String serialize(OutputStream stream, T value) {
        _outputStream = stream;

        String contentType = serialize(value);

        try {
            stream.flush();
        } catch (IOException e) {
        }
        return contentType;
    }
}
