package com.open.redis.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.open.redis.annotation.RedisLockAspectProcessor;
import com.open.redis.factory.RedisLockFactory;
import com.open.redis.service.RedisOperationService;
import com.open.redis.service.impl.RedisOperationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaoshengjie
 * @description  redis operation config to use register java object
 */

@Slf4j
@Configuration
public class RedisOperationConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setHashKeySerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    public RedisOperationManager redisOperationManager(RedisTemplate redisTemplate){
        return new RedisOperationManager(redisTemplate);
    }

    @Bean
    public RedisOperationService redisOperationService(RedisTemplate redisTemplate){
        return new RedisOperationServiceImpl(redisTemplate);
    }

    @Bean
    public RedisLockFactory redisLockFactory(RedisConnectionFactory redisConnectionFactory){
        return new RedisLockFactory(redisConnectionFactory);
    }

    @Bean
    public RedisLockManager redisLockManager(RedisLockFactory redisLockFactory){
        return new RedisLockManager(redisLockFactory);
    }

    @Bean
    public RedisLockAspectProcessor redisLockAspectProcessor(){
        return new RedisLockAspectProcessor();
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        return new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue(64));
    }
}
