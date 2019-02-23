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
package com.github.piaoruiqing.dlock.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.piaoruiqing.dlock.annotation.DistributedLockable;
import com.github.piaoruiqing.dlock.model.AnyObject;

/**
 *
 * @author piaoruiqing
 * @date: 2019/02/23 23:09
 *
 * @since JDK 1.8
 */
/**
 *
 * @author piaoruiqing
 * @date: 2019/05/17 23:10
 *
 * @since JDK 1.8
 */
@Service
public class DistributedLockableService {
    
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * @author piaoruiqing
     *
     * @param anyObject
     * @param param1
     * @param param2
     * @param timeout
     * @return
     */
    @DistributedLockable(
        argNames = {"anyObject.id", "anyObject.name", "param1"},
        timeout = 20, unit = TimeUnit.SECONDS
    )
    public Long distributedLockable(AnyObject anyObject, String param1, Object param2, Long timeout) {

        try {
            TimeUnit.SECONDS.sleep(timeout);
            LOGGER.info("distributed-lockable: " + System.nanoTime());
        } catch (InterruptedException e) {
        }

        return System.nanoTime();
    }
    
    /**
     * @author piaoruiqing
     *
     * @param anyObject
     * @param param1
     * @param param2
     * @param timeout
     * @return
     */
    @DistributedLockable(
        argNames = {"anyObject.id", "anyObject.name", "param1"},
        timeout = 20, unit = TimeUnit.SECONDS, 
        onFailure = RuntimeException.class
        )
    public Long distributedLockableOnFaiFailure(AnyObject anyObject, String param1, Object param2, Long timeout) {
        
        try {
            TimeUnit.SECONDS.sleep(timeout);
            LOGGER.info("distributed-lockable-on-failure: " + System.nanoTime());
        } catch (InterruptedException e) {
        }
        
        return System.nanoTime();
    }
}
