package com.open.redis.config;

import com.open.redis.service.RedisOperationService;
import com.open.redis.service.impl.RedisOperationServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisOperationManager {
    private RedisOperationService redisOperationService;

    public RedisOperationManager(RedisTemplate redisTemplate){
        redisOperationService = new RedisOperationServiceImpl(redisTemplate);
    }

    public RedisOperationService getRedisOperationService() {
        return this.redisOperationService;
    }
}
