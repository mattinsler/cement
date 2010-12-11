package com.mattinsler.cement;

import com.mattinsler.contract.ContractSerializationWriter;
import com.mattinsler.contract.IsContract;
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
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CementFormatSerializer {
    String getFormat();

    <T> String serialize(Appendable appendable, T value);
    <T> String serialize(OutputStream stream, T value);
}
