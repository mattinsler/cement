package com.mattinsler.contract;

import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import com.mattinsler.contract.guice.ContractMapperSerializerProvider;
import com.mattinsler.contract.json.JsonSerializationWriter;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/27/10
 * Time: 1:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ContractTest {
    private static class FooMapper extends ContractMapper<FooContract, Object> {
        @Override
        protected void mapContract(FooContract contract, Object value) {
            map(contract.foo()).to(new FieldMapper<Object, String>() {
                public String mapField(Object value) {
                    return "foo";
                }
            });
            map(contract.baz()).to(new FieldMapper<Object, Integer>() {
                public Integer mapField(Object value) {
                    return 4;
                }
            });
            map(contract.bar()).to(new FieldMapper<Object, Object>() {
                public Object mapField(Object value) {
                    return new Object();
                }
            });
        }
    }

    private static class BarMapper extends ContractMapper<BarContract, Object> {
        @Override
        protected void mapContract(BarContract contract, Object value) {
            map(contract.isCool()).to(new FieldMapper<Object, Boolean>() {
                public Boolean mapField(Object value) {
                    return true;
                }
            });
        }
    }

    @Test
    public void testSerializer() {
        Injector injector = Guice.createInjector(new Module() {
            public void configure(Binder binder) {
                Multibinder<ContractFormatter> serializers = Multibinder.newSetBinder(binder, ContractFormatter.class);
                serializers.addBinding().toProvider(new ContractMapperSerializerProvider(FooMapper.class));
                serializers.addBinding().toProvider(new ContractMapperSerializerProvider(BarMapper.class));

                Multibinder<ContractSerializationWriter> writers = Multibinder.newSetBinder(binder, ContractSerializationWriter.class);
                writers.addBinding().to(JsonSerializationWriter.class);
            }
        });

        Set<ContractFormatter> formatters = injector.getInstance(new Key<Set<ContractFormatter>>() {});
        Set<ContractSerializationWriter> writers = injector.getInstance(new Key<Set<ContractSerializationWriter>>() {});

        ContractSerializationService serializationService = injector.getInstance(ContractSerializationService.class);

        StringBuilder builder = new StringBuilder();
        serializationService.serialize(builder, FooContract.class, Arrays.asList(new Object(), new Object()), "json");
        System.out.println(builder);
    }
}
