package com.open.redis.config;


import com.open.redis.factory.RedisLockFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class RedisLockManager {
    private final RedisLockFactory redisLockFactory;
    private Lock lock;

    public RedisLockManager(RedisLockFactory redisLockFactory){
        this.redisLockFactory = redisLockFactory;
    }

    public RedisLockManager (RedisLockFactory redisLockFactory, String key, long expireAfter){
        this.redisLockFactory = redisLockFactory;
        this.lock = redisLockFactory.createLock(key, expireAfter);
    }

    public boolean acquire(String key, long expireAfter, TimeUnit timeUnit) {
        if (lock == null) {
            this.lock = redisLockFactory.createLock(key, expireAfter);
        }
        try {

            return lock.tryLock(expireAfter, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean release() {
        try {
            lock.unlock();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
