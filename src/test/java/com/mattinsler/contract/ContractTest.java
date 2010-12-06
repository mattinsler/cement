package com.mattinsler.contract;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mattinsler.contract.guice.ContractModule;
import com.mattinsler.contract.json.JsonSerializationWriter;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/27/10
 * Time: 1:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ContractTest {
    private static class FooMapper extends ContractMapper<Object, FooContract> {
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

    private static class BarMapper extends ContractMapper<Object, BarContract> {
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
        Injector injector = Guice.createInjector(new ContractModule() {
            @Override
            protected void configureContracts() {
                bindWriter(JsonSerializationWriter.class);

                bindMapper(FooMapper.class);
                bindMapper(BarMapper.class);
            }
        });

        ContractSerializationService serializationService = injector.getInstance(ContractSerializationService.class);

        StringBuilder builder = new StringBuilder();
//        serializationService.serialize(builder, FooContract.class, Arrays.asList(new Object(), new Object()), "json");
        serializationService.serialize(builder, FooContract.class, new Object(), "json");
        System.out.println(builder);
    }
}
