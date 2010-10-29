package com.mattinsler.cement.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 14, 2010
 * Time: 2:38:11 AM
 * To change this template use File | Settings | File Templates.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Post {
    String defaultFormat() default "";
}
