package com.mattinsler.contract.formatter;

import com.mattinsler.contract.ContractSerializationContext;
import com.mattinsler.contract.ContractSerializationWriter;
import com.mattinsler.contract.ValueMetadata;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/5/10
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class IntegerFormatter extends AbstractValueFormatter<Integer> {
    public IntegerFormatter() {
        super(Integer.class);
    }

    @Override
    public void format(ContractSerializationWriter writer, Integer value, ValueMetadata metadata, ContractSerializationContext context) {
        writer.writeValue(value.toString());
    }
}
