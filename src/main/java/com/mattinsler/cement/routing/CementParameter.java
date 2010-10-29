package com.mattinsler.cement.routing;

import com.google.inject.BindingAnnotation;
import com.google.inject.Key;
import com.mattinsler.cement.annotation.Parameter;
import com.mattinsler.cement.converter.Converter;
import com.mattinsler.cement.converter.ConverterRegistry;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 29, 2010
 * Time: 2:21:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class CementParameter<T> {
    public String name;
    public int index;
    public Class<T> type;
    public Key<T> bindingKey;
    public Converter<T> converter;
    public List<Annotation> annotations = new ArrayList<Annotation>();
//    public T defaultValue; - maybe?


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CementParameter)) {
            return false;
        }
        CementParameter<?> other = (CementParameter<?>)obj;
        return (
                this.name.equals(other.name)
                && this.index == other.index
                && this.type.equals(other.type)
        );
    }

    public static <T> CementParameter<T> create(int index, Class<T> type, Annotation[] annotations) {
        CementParameter<T> parameter = new CementParameter<T>();

        parameter.index = index;
        parameter.type = type;

        for (Annotation a : annotations) {
            if (a instanceof Parameter) {
                parameter.name = ((Parameter)a).value();
            } else {
                parameter.annotations.add(a);
                if (a instanceof BindingAnnotation) {
                    parameter.bindingKey = Key.get(type, a);
                }
            }
        }

        if (parameter.name == null) {
            parameter.bindingKey = Key.get(type);
        } else {
            // figure out the correct converter
            parameter.converter = ConverterRegistry.get().getConverter(parameter.type);
        }

        return parameter;
    }
}
