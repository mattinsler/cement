package com.mattinsler.cement.annotation;

import com.mattinsler.cement.CementFormatSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/10/10
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Serializer {
    String format();
    Class<? extends CementFormatSerializer> serializer();
}
