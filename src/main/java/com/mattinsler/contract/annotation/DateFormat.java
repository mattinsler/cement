package com.mattinsler.contract.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/4/10
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormat {
    /**
     * Format to be passed to SimpleDateFormat
     * @see {@link http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html}
     */
    String value();
}
