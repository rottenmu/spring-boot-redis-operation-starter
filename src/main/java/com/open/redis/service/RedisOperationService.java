package com.open.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


/**
 * redis getValue(String key, Callable<V> callable)
 * 最终优化
 *
 * redis.get <yes/no>=> redisNoneKeyQueue => EventListener[收到addKey Event process event]
 *         ||
 *      return Value
 *
 * redis operation service
 * @author zhaoshengjie
 */
public interface RedisOperationService{

     <K, V> RedisTemplate<K, V> getRedisTemplate();

    default <K, V> ValueOperations<K, V> getStringOperations() {
        return (ValueOperations<K, V>) getRedisTemplate().opsForValue();
    }

    default boolean hasKey(String key) {
        return getRedisTemplate().hasKey(key);
    }

    default void set(String key, Object value) {
        getStringOperations().set(key, value);
    }

    default void set(String key, Object value, long timeout, TimeUnit unit) {
        getStringOperations().set(key, value, timeout, unit);
    }

    default <V> Optional<V> getValue(String key) {
        return Optional.<V>ofNullable((V) getStringOperations().get(key));
    }

    default <V> Optional<V> getValue(String key, Callable<V> callable) {
        return Optional.ofNullable((V) getValue(key).orElseGet(() -> {
            V v = null;
            try {
                Assert.notNull(callable, "callable function is null");
               v = callable.call();
                getStringOperations().set(key, v);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return v;
        }));
    }

    default <V> Optional<V> getValue(String key, CompletableFuture<V> future, long timeout, TimeUnit unit) {
        return Optional.ofNullable((V) getValue(key).orElseGet(() -> {
            V v = null;
            try {
                Assert.notNull(future, "future function is null");
                v = future.get(timeout, unit);
                getStringOperations().set(key, v);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return v;
        }));
    }

    default Long increment(String key) {
        return getStringOperations().increment(key);
    }

    default Long increment(String key, long delta) {
        return getStringOperations().increment(key, delta);
    }

    default Long decrement(String key) {
        return getStringOperations().increment(key);
    }

    default Long decrement(String key, long delta) {
        return getStringOperations().increment(key, delta);
    }

    default boolean deleteKey(String key) {
        return getRedisTemplate().delete(key);
    }

    default boolean deleteKeys(Iterable<String> keys) {
        return getRedisTemplate().delete(keys);
    }

    default void sendMessage(String topic, Object message){
        getRedisTemplate().convertAndSend(topic, message);
    }

}
