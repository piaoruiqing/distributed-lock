/*
 * 
 * Copyright 2019 piaoruiqing
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.github.piaoruiqing.dlock.aspect;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import com.github.piaoruiqing.dlock.RedisLockClient;
import com.github.piaoruiqing.dlock.annotation.DistributedLockable;
import com.github.piaoruiqing.dlock.support.KeyGenerator;

/**
 *
 * @author piaoruiqing
 * @date: 2019/02/02 22:35
 *
 * @since JDK 1.8
 */
@Aspect
@Order(10)
public class DistributedLockableAspect implements KeyGenerator {
    
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @Resource
    private RedisLockClient redisLockClient;
    
    
    /**
     * {@link DistributedLockable}
     * @author piaoruiqing
     *
     */
    @Pointcut(value = "execution(* *(..)) && @annotation(com.github.piaoruiqing.dlock.annotation.DistributedLockable)")
    public void distributedLockable() {}
    
    /**
     * @author piaoruiqing
     *
     * @param joinPoint
     * @param lockable
     * @return
     * @throws Throwable
     */
    @Around(value = "distributedLockable() && @annotation(lockable)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLockable lockable) throws Throwable {

        long start = System.nanoTime();
        final String key = this.generate(joinPoint, lockable.prefix(), lockable.argNames(), lockable.argsAssociated()).toString();

        Object result = redisLockClient.tryLock(
            key, () -> {
                return joinPoint.proceed();
            }, 
            lockable.unit().toMillis(lockable.timeout()), lockable.autoUnlock(), 
            lockable.retries(), lockable.unit().toMillis(lockable.waitingTime()),
            lockable.onFailure()
        );

        long end = System.nanoTime();
        LOGGER.debug("distributed lockable cost: {} ns", end - start);

        return result;
    }

}
