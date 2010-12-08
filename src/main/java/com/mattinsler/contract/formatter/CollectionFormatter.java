package com.mattinsler.contract.formatter;

import com.mattinsler.contract.ContractSerializationContext;
import com.mattinsler.contract.ContractSerializationWriter;
import com.mattinsler.contract.ValueMetadata;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/5/10
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollectionFormatter extends AbstractValueFormatter<Collection> {
    public CollectionFormatter() {
        super(Collection.class);
    }

    @Override
    public void format(ContractSerializationWriter writer, Collection value, ValueMetadata metadata, ContractSerializationContext context) {
        writer.beginList();

        for (Object item : value) {
            context.formatValue(writer, item, metadata);
        }

        writer.endList();
    }
}
