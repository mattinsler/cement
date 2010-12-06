package com.mattinsler.contract;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ContractSerializationWriter {
    String getFormat();
    String getContentType();
    void setOutput(Appendable appendable);

    void begin();
    void end();

    void beginList();
    void endList();

    void beginElement(String name);
    void endElement();

    void writeValue(String value);
}
