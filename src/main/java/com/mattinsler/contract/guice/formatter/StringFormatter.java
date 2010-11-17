package com.mattinsler.contract.guice.formatter;

import com.mattinsler.contract.ValueFormatter;
import com.mattinsler.contract.ContractSerializationWriter;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/4/10
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringFormatter implements ValueFormatter<String> {
    public void format(ContractSerializationWriter writer, String value) {
        writer.writeValue("'" + value + "'");
    }
}
