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
package com.github.piaoruiqing.dlock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * distributed lock
 *
 * @author piaoruiqing
 * @date: 2019/02/02 22:28
 *
 * @since JDK 1.8
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributedLockable {

    /**
     * timeout of the lock
     * 
     * @author piaoruiqing
     *
     * @return
     */
    long timeout() default 5L;

    /**
     * time unit
     * @author piaoruiqing
     *
     * @return
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;

    /**
     * number of retries
     * 
     * @author piaoruiqing
     *
     * @return
     */
    int retries() default 0;

    /**
     * interval of each retry
     * 
     * @author piaoruiqing
     *
     * @return
     */
    long waitingTime() default 0L;

    /**
     * key prefix
     * 
     * @author piaoruiqing
     *
     * @return
     */
    String prefix() default "";

    /**
     * parameters that construct a key
     * 
     * @author piaoruiqing
     *
     * @return
     */
    String[] argNames() default {};

    /**
     * construct a key with parameters
     * 
     * @author piaoruiqing
     *
     * @return
     */
    boolean argsAssociated() default true;

    /**
     * whether unlock when completed
     * 
     * @author piaoruiqing
     *
     * @return
     */
    boolean autoUnlock() default true;

    /**
     * throw an runtime exception while fail to get lock
     * 
     * @author piaoruiqing
     *
     * @return
     */
    Class<? extends RuntimeException> onFailure() default NoException.class;

    /**
     * no exception
     *
     * @author piaoruiqing
     * @date: 2019/05/18 09:31
     *
     * @since JDK 1.8
     */
    public static final class NoException extends RuntimeException {

        private static final long serialVersionUID = -7821936618527445658L;

    }
}
