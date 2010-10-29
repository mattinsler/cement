package com.mattinsler.cement.contract;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/17/10
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataContractSerializationWriter {
    String get();

    void begin();
    void end();
    void beginElement(String name);
    <T> void writeValue(T value);
    void endElement();
}
