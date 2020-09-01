package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @AnnotationName KaptchaConfig
 * @Description 登录标记注解,标记该注解的方法必须是在用户登录之后才可以调用
 * @Author cxc
 * @Date 2020/9/1 0:32
 * @Verseion 1.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {

}
