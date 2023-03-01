package com.cheche.annotation;

import java.lang.annotation.*;

/**
 * 序列化注解
 *
 * @author cheche
 * @date 2022/12/29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SerializerClass {

  String value() default "";

  String description() default "";

}
