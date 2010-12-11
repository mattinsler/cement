package com.mattinsler.contract;

import com.google.inject.Inject;
import com.mattinsler.contract.exception.DuplicateWriterException;
import com.mattinsler.contract.exception.UnknownFormatException;
import com.mattinsler.contract.exception.UnknownSerializerException;
import com.mattinsler.contract.formatter.ContractFormatter;
import com.mattinsler.contract.formatter.ValueFormatter;

import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContractSerializationService {
    private final ContractSerializationContext _context = new ContractSerializationContext();
    private final Map<String, ContractSerializationWriter> _writers = new HashMap<String, ContractSerializationWriter>();

    @Inject
    ContractSerializationService(Set<ValueFormatter> formatters, Set<ContractFormatter> contractFormatters, Set<ContractSerializationWriter> writers) {
        for (ValueFormatter formatter : formatters) {
            _context.addFormatter(formatter);
        }

        for (ContractFormatter formatter : contractFormatters) {
            _context.addContractFormatter(formatter);
        }

        for (ContractSerializationWriter writer : writers) {
            if (_writers.containsKey(writer.getFormat())) {
                throw new DuplicateWriterException(writer.getFormat());
            }
            _writers.put(writer.getFormat(), writer);
        }
    }

    /**
     * Serializes value into appendable, returning the content type for format
     * @param appendable
     * @param value
     * @param contract
     * @param format
     * @param <T>
     * @param <C>
     * @return content type
     */
    public <T, C extends IsContract> String serialize(Appendable appendable, Class<C> contract, T value, String format) throws UnknownFormatException, UnknownSerializerException {
        ContractSerializationWriter writer = _writers.get(format);
        if (writer == null) {
            throw new UnknownFormatException(format);
        }

        writer.setOutput(appendable);
        _context.formatContract(writer, value, contract);
        if (appendable instanceof Flushable) {
            try {
                ((Flushable)appendable).flush();
            } catch (IOException e) {
            }
        }
        return writer.getContentType();
    }

    /**
     * Serializes value into stream, returning the content type for format
     * @param stream
     * @param value
     * @param contract
     * @param format
     * @param <T>
     * @param <C>
     * @return content type
     */
    public <T, C extends IsContract> String serialize(OutputStream stream, Class<C> contract, T value, String format) throws UnknownFormatException, UnknownSerializerException {
        String contentType = serialize(new OutputStreamWriter(stream), contract, value, format);
        try {
            stream.flush();
        } catch (IOException e) {
        }
        return contentType;
    }

    public <T> String serialize(Appendable appendable, T value, String format) throws UnknownFormatException, UnknownSerializerException {
        ContractSerializationWriter writer = _writers.get(format);
        if (writer == null) {
            throw new UnknownFormatException(format);
        }

        writer.setOutput(appendable);
        _context.formatValue(writer, value, new ValueMetadata());
        if (appendable instanceof Flushable) {
            try {
                ((Flushable)appendable).flush();
            } catch (IOException e) {
            }
        }
        return writer.getContentType();
    }

    public <T> String serialize(OutputStream stream, T value, String format) throws UnknownFormatException, UnknownSerializerException {
        String contentType = serialize(new OutputStreamWriter(stream), value, format);
        try {
            stream.flush();
        } catch (IOException e) {
        }
        return contentType;
    }
}
