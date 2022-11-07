package com.open.redis.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Shengjie ZHao
 * @date 2022-11-07
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLock {
    boolean fair() default true;

    int expireTime() default 10;

    int timeout() default 10;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    String key();

}
