package com.mattinsler.cement.annotation;

import com.mattinsler.cement.CementResponseWriter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 21, 2010
 * Time: 12:46:33 AM
 * To change this template use File | Settings | File Templates.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseWriters {
    Class<? extends CementResponseWriter>[] value();
}
