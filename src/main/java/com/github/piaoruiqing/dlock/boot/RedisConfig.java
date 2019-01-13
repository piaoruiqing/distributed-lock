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
package com.github.piaoruiqing.dlock.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.github.piaoruiqing.dlock.RedisLockClient;
import com.github.piaoruiqing.dlock.aspect.DistributedLockableAspect;

/**
 * redis config
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/01/12 23:19
 *
 * @since JDK 1.8
 */
@Configuration
public class RedisConfig implements ImportAware {

    /**
     * @author piaoruiqing
     *
     * @return
     */
    @Bean
    public DistributedLockableAspect distributedLockableAspect() {

        return new DistributedLockableAspect();
    }

    /**
     * @author piaoruiqing
     *
     * @return
     */
    @Bean
    public RedisLockClient redisLockClient() {

        return new RedisLockClient();
    }

    /**
     * @author piaoruiqing
     *
     * @param factory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        RedisSerializer<?> serializer = new StringRedisSerializer();
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();

        return template;

    }

    /* (non-Javadoc)
     * @see org.springframework.context.annotation.ImportAware#setImportMetadata(org.springframework.core.type.AnnotationMetadata)
     */
    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        
    }
}
