package com.mattinsler.cement.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 17, 2010
 * Time: 10:12:00 AM
 * To change this template use File | Settings | File Templates.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
    String value();
    // Object defaultValue() ?
}
