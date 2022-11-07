package com.open.redis.factory;


import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.util.Objects;
import java.util.concurrent.locks.Lock;

/**
 * @author Shengjie Zhao
 * @version 1.0
 */
public class RedisLockFactory extends AbstractRedisLockFactory {
    private final RedisConnectionFactory redisConnectionFactory;
    private RedisLockRegistry redisLockRegistry;

    public RedisLockFactory(RedisConnectionFactory redisConnectionFactory){
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public Lock createLock(String key, long expireAfter) {
        RedisLockRegistry redisLockRegistry = new RedisLockRegistry(redisConnectionFactory, key, expireAfter);
        this.redisLockRegistry = redisLockRegistry;
        return redisLockRegistry.obtain(key);
    }

    @Override
    public void destroy() {
        if (Objects.nonNull(redisLockRegistry)) {
            redisLockRegistry.destroy();
        }
    }
}
