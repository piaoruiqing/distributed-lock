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
package com.github.piaoruiqing.dlock.lock;

import java.util.Collections;
import java.util.List;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * redis distributed lock
 *
 * @author piaoruiqing
 * @date: 2019/01/12 23:20
 *
 * @since JDK 1.8
 */
public class RedisDistributedLock extends DistributedLock {

    private RedisOperations<String, String> operations;
    private String key;
    private String value;

    private static final String COMPARE_AND_DELETE =
        "if redis.call('get',KEYS[1]) == ARGV[1]\n" +
        "then\n" +
        "    return redis.call('del',KEYS[1])\n" +
        "else\n" +
        "    return 0\n" +
        "end";
    
    /**
     * @param operations
     * @param key
     * @param value
     */
    public RedisDistributedLock(RedisOperations<String, String> operations, String key, String value) {

        this.operations = operations;
        this.key = key;
        this.value = value;
    }

    /* (non-Javadoc)
     * @see com.github.piaoruiqing.dlock.lock.DistributedLock#release()
     */
    @Override
    public void release() {

        List<String> keys = Collections.singletonList(key);
        operations.execute(new DefaultRedisScript<Object>(COMPARE_AND_DELETE, Object.class), keys, value);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RedisDistributedLock [key=" + key + ", value=" + value + "]";
    }

}
