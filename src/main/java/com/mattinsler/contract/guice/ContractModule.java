package com.mattinsler.contract.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.mattinsler.contract.*;
import com.mattinsler.contract.formatter.BooleanFormatter;
import com.mattinsler.contract.formatter.IntegerFormatter;
import com.mattinsler.contract.formatter.ListFormatter;
import com.mattinsler.contract.formatter.StringFormatter;
import com.mattinsler.contract.option.ContractOption;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/29/10
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ContractModule extends AbstractModule {
    protected void bindMapper(Class<? extends ContractMapper> mapperType) {
        Multibinder.newSetBinder(binder(), ContractFormatter.class).addBinding().toProvider(new ContractMapperSerializerProvider(mapperType));

        for (Class<?> internalClass : mapperType.getDeclaredClasses()) {
            if (ContractMapper.class.isAssignableFrom(internalClass)) {
                bindMapper((Class<? extends ContractMapper>)internalClass);
            }
        }
    }

    protected void bindWriter(Class<? extends ContractSerializationWriter> writerType) {
        Multibinder.newSetBinder(binder(), ContractSerializationWriter.class).addBinding().to(writerType);
    }

    protected void registerOption(Class<? extends ValueMetadata.Option> optionType) {
        Multibinder.newSetBinder(binder(), ValueMetadata.Option.class).addBinding().to(optionType);
    }

    protected void registerFormatter(Class<? extends ValueFormatter> formatterType) {
        Multibinder.newSetBinder(binder(), ValueFormatter.class).addBinding().to(formatterType);
    }

    @Override
    protected void configure() {
        bind(MetadataService.class).in(Singleton.class);

        registerFormatter(BooleanFormatter.class);
        registerFormatter(IntegerFormatter.class);
        registerFormatter(ListFormatter.class);
        registerFormatter(StringFormatter.class);

        registerOption(ContractOption.class);

        configureContracts();
    }

    protected abstract void configureContracts();
}
