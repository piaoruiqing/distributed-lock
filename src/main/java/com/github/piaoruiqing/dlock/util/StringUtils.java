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
package com.github.piaoruiqing.dlock.util;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author piaoruiqing
 * @date: 2019/02/02 22:38
 *
 * @since JDK 1.8
 */
abstract public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final ObjectMapper OBJECT_MAPPER;
    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * @author piaoruiqing
     *
     * @param builder
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static StringBuilder appendObject(StringBuilder builder, Object... object) throws JsonProcessingException {

        for (Object item : object) {
            if (item instanceof Number || item instanceof String || item instanceof Boolean
                || item instanceof Character) {
                builder.append(item);
            } else {
                builder.append(OBJECT_MAPPER.writeValueAsString(item));
            }
        }
        return builder;
    }

    /**
     * @author piaoruiqing
     *
     * @param argNames
     * @param args
     * @param separatorKV
     * @param separator
     * @return
     * @throws JsonProcessingException
     */
    public static StringBuilder simpleJoinToBuilder(String[] argNames, Object[] args, String separatorKV,
        String separator) throws JsonProcessingException {

        if (argNames == null || args == null) {
            return null;
        }
        if (argNames.length != args.length) {
            throw new IllegalArgumentException("inconsistent parameter length !");
        }
        if (argNames.length <= 0) {
            return new StringBuilder(0);
        }
        int bufSize = argNames.length * (argNames[0].toString().length()
            + Optional.ofNullable(args[0]).map(String::valueOf).map(String::length).orElse(4) + 2);
        StringBuilder builder = new StringBuilder(bufSize);
        for (int index = 0; index < argNames.length; index++) {
            if (index > 0) {
                builder.append(separator);
            }
            appendObject(builder, argNames[index], separatorKV, args[index]);
        }

        return builder;
    }

}
