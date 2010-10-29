package com.mattinsler.cement.annotation;

import com.mattinsler.cement.IsDataContract;
import com.mattinsler.cement.contract.CementErrorContract;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/16/10
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataContract {
    Class<? extends IsDataContract> value();
    Class<? extends IsDataContract> error() default CementErrorContract.class;
}
