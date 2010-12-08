package com.mattinsler.contract.formatter;

import com.google.inject.Inject;
import com.mattinsler.contract.ContractSerializationContext;
import com.mattinsler.contract.ContractSerializationWriter;
import com.mattinsler.contract.ValueMetadata;

import java.util.List;
import java.util.RandomAccess;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Dec 6, 2010
 * Time: 2:02:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class RandomAccessFormatter extends AbstractValueFormatter<RandomAccess> {
    private final CollectionFormatter _collectionFormatter;

    @Inject
    RandomAccessFormatter(CollectionFormatter collectionFormatter) {
        super(RandomAccess.class);
        _collectionFormatter = collectionFormatter;
    }

    @Override
    public void format(ContractSerializationWriter writer, RandomAccess value, ValueMetadata metadata, ContractSerializationContext context) {
        _collectionFormatter.format(writer, (List)value, metadata, context);
    }
}
