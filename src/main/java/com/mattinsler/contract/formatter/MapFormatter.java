package com.mattinsler.contract.formatter;

import com.mattinsler.contract.ContractSerializationContext;
import com.mattinsler.contract.ContractSerializationWriter;
import com.mattinsler.contract.ValueMetadata;

import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Dec 6, 2010
 * Time: 5:46:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapFormatter extends AbstractValueFormatter<Map> {
    MapFormatter() {
        super(Map.class);
    }

    @Override
    public void format(ContractSerializationWriter writer, Map value, ValueMetadata metadata, ContractSerializationContext context) {
        writer.begin();

        for (Map.Entry<?, ?> entry : (Set<Map.Entry<?, ?>>)value.entrySet()) {
            writer.beginElement(entry.getKey().toString());
            context.formatValue(writer, entry.getValue(), metadata);
            writer.endElement();
        }

        writer.end();
    }
}
