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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.piaoruiqing.dlock.model.AnyObject;
import com.github.piaoruiqing.dlock.service.DistributedLockableService;

/**
 *
 * @author piaoruiqing
 * @date: 2019/02/23 23:13
 *
 * @since JDK 1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DistributedLockableTest {

    private static final ExecutorService POOL = Executors.newFixedThreadPool(32);

    @Resource
    private DistributedLockableService distributedLockableService;

    /**
     * @author piaoruiqing
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void testDistributedLockable() throws InterruptedException, ExecutionException {

        AnyObject anyObject =
            AnyObject.builder().name(RandomStringUtils.random(3)).id(ThreadLocalRandom.current().nextLong()).build();

        List<Future<Long>> list = new ArrayList<>();
        
        for (int index = 0; index < 100; index++) {
            list.add(POOL.submit(() -> 
                distributedLockableService.distributedLockable(anyObject, "str", "str", 20L)
            ));
        }

        Set<Long> results = new HashSet<>();
        for (Future<Long> future : list) {
            if (future.get() != null) {
                results.add(future.get());
            }
        }

        assertTrue(results.size() == 1);
    }
    
    /**
     * @author piaoruiqing
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void testOnFailure() throws InterruptedException, ExecutionException {
        
        AnyObject anyObject =
            AnyObject.builder().name(RandomStringUtils.random(3)).id(ThreadLocalRandom.current().nextLong()).build();

        List<Exception> exceptions = new ArrayList<>();
        Future<?> future1 = POOL.submit(() -> {
            try {
                distributedLockableService.distributedLockableOnFaiFailure(anyObject, "str", "str", 20L);
            } catch (Exception e) {
                exceptions.add(e);
            }
        });
        Future<?> future2 = POOL.submit(() -> {
            try {
                distributedLockableService.distributedLockableOnFaiFailure(anyObject, "str", "str", 20L);
            } catch (Exception e) {
                exceptions.add(e);
            }
        });
        future1.get();
        future2.get();
        assertEquals(exceptions.size(), 1);
        assertEquals(exceptions.get(0).getClass(), RuntimeException.class);
    }

}
