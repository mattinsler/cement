package com.mattinsler.contract.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.mattinsler.contract.*;
import com.mattinsler.contract.formatter.*;
import com.mattinsler.contract.option.ContractOption;
import com.mattinsler.guiceytools.ConfigureOnceModule;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/29/10
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ContractModule implements Module {
    private Binder _binder;

    protected void bindMapper(Class<? extends ContractMapper> mapperType) {
        Multibinder.newSetBinder(_binder, ContractFormatter.class).addBinding().toProvider(new ContractMapperSerializerProvider(mapperType));

        for (Class<?> internalClass : mapperType.getDeclaredClasses()) {
            if (ContractMapper.class.isAssignableFrom(internalClass)) {
                bindMapper((Class<? extends ContractMapper>)internalClass);
            }
        }
    }

    protected void bindWriter(Class<? extends ContractSerializationWriter> writerType) {
        Multibinder.newSetBinder(_binder, ContractSerializationWriter.class).addBinding().to(writerType);
    }

    protected void registerOption(Class<? extends ValueMetadata.Option> optionType) {
        Multibinder.newSetBinder(_binder, ValueMetadata.Option.class).addBinding().to(optionType);
    }

    protected void registerFormatter(Class<? extends ValueFormatter> formatterType) {
        Multibinder.newSetBinder(_binder, ValueFormatter.class).addBinding().to(formatterType);
    }

    public void configure(Binder binder) {
        _binder = binder.skipSources(ContractModule.class);

        _binder.install(new ConfigureOnceModule<Class>(ContractModule.class) {
            public void configure(Binder binder) {
                binder.bind(MetadataService.class).in(Singleton.class);
            }
        });

        registerFormatter(BooleanFormatter.class);
        registerFormatter(CollectionFormatter.class);
        registerFormatter(IntegerFormatter.class);
        registerFormatter(MapFormatter.class);
        registerFormatter(StringFormatter.class);

        registerOption(ContractOption.class);

        configureContracts();
    }

    protected abstract void configureContracts();
}
