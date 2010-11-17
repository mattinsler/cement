package com.mattinsler.contract;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/3/10
 * Time: 1:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class ListValueFormatter extends AbstractValueFormatter<List> {
    public ListValueFormatter() {
        super(List.class);
    }

    public void format(ContractSerializationWriter writer, List value, ContractSerializationContext context) {
        writer.beginList();

        for (Object item : value) {
            context.formatValue(writer, item);
        }

        writer.endList();
    }
}
