package com.open.redis.annotation;

import com.open.redis.config.RedisLockManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

@Slf4j
@Aspect
public class RedisLockAspectProcessor implements Ordered {
    @Autowired
    private RedisLockManager redisLockManager;

    @Pointcut(value = "@annotation(com.ljhy.clinic.common.redis.operation.annotation.RedisLock)")
    public void pointcut(){

    }

    @Around("pointcut()")
    public Object process(ProceedingJoinPoint joinPoint){
        Object result = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        if (redisLock != null) {

            log.info("......prepare to get lock");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean acquire = redisLockManager.acquire(redisLock.key(), redisLock.timeout(), redisLock.timeUnit());
            log.info("lock.status......{}",acquire);
            if (acquire){
                try {
                    result = joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }finally {

                    log.info("......start release lock");
                    boolean release = redisLockManager.release();
                    log.info("lock.status......{}",release);
                }
            }
        }

        return result;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
