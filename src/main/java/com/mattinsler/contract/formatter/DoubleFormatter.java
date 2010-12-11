package com.mattinsler.contract.formatter;

import com.mattinsler.contract.ContractSerializationContext;
import com.mattinsler.contract.ContractSerializationWriter;
import com.mattinsler.contract.ValueMetadata;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Dec 8, 2010
 * Time: 2:32:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DoubleFormatter extends AbstractValueFormatter<Double> {
    DoubleFormatter() {
        super(Double.class);
    }

    @Override
    public void format(ContractSerializationWriter writer, Double value, ValueMetadata metadata, ContractSerializationContext context) {
        writer.writeValue(value.toString());
    }
}
