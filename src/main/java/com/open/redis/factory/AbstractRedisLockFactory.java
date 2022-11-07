package com.open.redis.factory;

import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.locks.Lock;

/**
 * @author shengjie
 */
public abstract class AbstractRedisLockFactory implements DisposableBean {
    /**
     * create redis lock
     * @param key
     * @return
     */
    public abstract Lock createLock(String key, long expireAfter);

}
