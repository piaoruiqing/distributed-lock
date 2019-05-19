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
package com.github.piaoruiqing.dlock;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.github.piaoruiqing.dlock.annotation.DistributedLockable.NoException;
import com.github.piaoruiqing.dlock.handler.LockHandler;
import com.github.piaoruiqing.dlock.lock.DistributedLock;
import com.github.piaoruiqing.dlock.lock.RedisDistributedLock;

/**
 *
 * @author piaoruiqing
 * @date: 2019/01/13 23:50
 *
 * @since JDK 1.8
 */
public class RedisLockClient {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * try lock
     * 
     * @author piaoruiqing
     *
     * @param <T>
     * @param key           lock key
     * @param handler       logic you want to execute
     * @param timeout       timeout
     * @param autoUnlock    whether unlock when completed
     * @param retries       number of retries
     * @param waitingTime   retry interval
     * @param onFailure     throw an runtime exception while fail to get lock
     * @return
     */
    public <T> T tryLock(String key, LockHandler<T> handler, long timeout, boolean autoUnlock, int retries, long waitingTime,
        Class<? extends RuntimeException> onFailure) throws Throwable {

        try (DistributedLock lock = this.acquire(key, timeout, retries, waitingTime);) {
            if (lock != null) {
                LOGGER.debug("get lock success, key: {}", key);
                return handler.handle();
            }
            LOGGER.debug("get lock fail, key: {}", key);
            if (null != onFailure && onFailure != NoException.class) {
                throw onFailure.newInstance();
            }
            return null;
        }
    }

    /**
     * acquire distributed  lock
     * 
     * @author piaoruiqing
     *
     * @param key           lock key
     * @param timeout       timeout
     * @param retries       number of retries
     * @param waitingTime   retry interval
     * @return
     * @throws InterruptedException
     */
    public DistributedLock acquire(String key, long timeout, int retries, long waitingTime) throws InterruptedException {

        final String value = RandomStringUtils.randomAlphanumeric(4) + System.currentTimeMillis();
        do {
            Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.MILLISECONDS);
            if (result) {
                return new RedisDistributedLock(stringRedisTemplate, key, value);
            }
            if (retries > NumberUtils.INTEGER_ZERO) {
                TimeUnit.MILLISECONDS.sleep(waitingTime);
            }
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        } while (retries-- > NumberUtils.INTEGER_ZERO);

        return null;
    }

}
