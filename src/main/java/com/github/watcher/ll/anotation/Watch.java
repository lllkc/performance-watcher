package com.github.watcher.ll.anotation;

import java.lang.annotation.*;

/**
 * @author: linlei
 * @date: 2016/10/23.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Watch {
    int threshold() default -1;
}
