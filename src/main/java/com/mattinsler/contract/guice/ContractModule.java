package com.mattinsler.contract.guice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.mattinsler.contract.ContractFormatter;
import com.mattinsler.contract.ContractSerializationWriter;
import com.mattinsler.contract.ContractMapper;
import com.mattinsler.contract.ValueMetadata;
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

    @Override
    protected void configure() {
        registerOption(ContractOption.class);
        configureContracts();
    }

    protected abstract void configureContracts();
}
