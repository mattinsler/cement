package com.mattinsler.contract.formatter;

import com.mattinsler.contract.ContractSerializationContext;
import com.mattinsler.contract.ContractSerializationWriter;
import com.mattinsler.contract.ValueMetadata;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/5/10
 * Time: 6:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringFormatter extends AbstractValueFormatter<String> {
    public StringFormatter() {
        super(String.class);
    }

    public void format(ContractSerializationWriter writer, String value, ValueMetadata metadata, ContractSerializationContext context) {
        writer.writeValue("'" + value + "'");
    }
}
