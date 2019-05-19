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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * distributed lock
 *
 * @author piaoruiqing
 * @date: 2019/01/12 23:15
 *
 * @since JDK 1.8
 */
abstract public class DistributedLock implements AutoCloseable {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * release lock
     * @author piaoruiqing
     */
    abstract public void release();

    /*
     * (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {

        LOGGER.debug("distributed lock close , {}", this.toString());

        this.release();
    }
}
