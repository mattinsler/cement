package com.mattinsler.contract;

import com.google.inject.Inject;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/5/10
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetadataService {
    private final Set<ValueMetadata.Option> _options;

    @Inject
    MetadataService(Set<ValueMetadata.Option> options) {
        _options = options;
    }

    ValueMetadata createMetadata(Method method) {
        ValueMetadata metadata = new ValueMetadata();

        for (ValueMetadata.Option option : _options) {
            option.recognize(method, metadata);
        }

        return metadata;
    }
}
