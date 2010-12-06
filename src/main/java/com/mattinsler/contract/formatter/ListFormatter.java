package com.mattinsler.contract.formatter;

import com.mattinsler.contract.ContractSerializationContext;
import com.mattinsler.contract.ContractSerializationWriter;
import com.mattinsler.contract.ValueMetadata;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/5/10
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListFormatter extends AbstractValueFormatter<List> {
    public ListFormatter() {
        super(ArrayList.class, LinkedList.class);
    }

    @Override
    public void format(ContractSerializationWriter writer, List value, ValueMetadata metadata, ContractSerializationContext context) {
        writer.beginList();

        for (Object item : value) {
            context.formatValue(writer, item, metadata);
        }

        writer.endList();
    }
}
