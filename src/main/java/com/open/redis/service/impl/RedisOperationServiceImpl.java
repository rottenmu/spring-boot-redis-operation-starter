package com.open.redis.service.impl;

import com.open.redis.service.RedisOperationService;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisOperationServiceImpl implements RedisOperationService {
    private final RedisTemplate redisTemplate;

    public RedisOperationServiceImpl(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <K, V> RedisTemplate<K, V> getRedisTemplate() {
        return this.redisTemplate;
    }

}
