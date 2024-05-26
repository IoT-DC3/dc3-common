/*
 * Copyright 2016-present the IoT DC3 original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.pnoker.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import io.github.pnoker.common.constant.AuthConstant;
import io.github.pnoker.common.constant.common.ExceptionConstant;
import io.github.pnoker.common.constant.common.PrefixConstant;
import io.github.pnoker.common.constant.common.SuffixConstant;
import io.github.pnoker.common.constant.common.SymbolConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @author linys
 * @since 2022.1.0
 */
public class AuthCacheUtil {

    private static final RedisTemplate redisTemplate = SpringUtil.getBean(RedisTemplate.class);

    private AuthCacheUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 获取Redis User key
     *
     * @param suffix   类型, SuffixConstant
     * @param tenantId 租户id
     * @param userName 用户名称
     * @return key
     */
    private static String getKey(String suffix, Long tenantId, String userName) {
        return PrefixConstant.USER + suffix + SymbolConstant.COLON + userName + SymbolConstant.HASHTAG + tenantId;
    }

    /**
     * 获取 用户 - token 映射Key
     *
     * @param tenantId 租户ID
     * @param userName 用户名
     * @return key
     */
    public static String getUserTokenKey(Long tenantId, String userName) {
        return getKey(SuffixConstant.TOKEN, tenantId, userName);
    }


    /**
     * 获取Redis Token Key
     *
     * @param token token
     * @return token key
     */
    public static String getTokenKey(String token) {
        return AuthConstant.redis_prefix + SymbolConstant.COLON + token;
    }

    /**
     * 获取Salt key
     *
     * @param tenantId 租户id
     * @param userName 用户名称
     * @return Salt key
     */
    public static String getSaltKey(Long tenantId, String userName) {
        return getKey(SuffixConstant.SALT, tenantId, userName);
    }

    /**
     * 查询redis value
     *
     * @param key key
     * @param <T> Value Type
     * @return Value Type
     */
    public static <T> T getValue(String key) {
        ValueOperations<String, T> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 添加 Key 缓存,并设置失效时间
     *
     * @param key   String key
     * @param value Object
     * @param time  Time
     * @param unit  TimeUnit
     * @param <T>   Value Type
     */
    public static <T> void setValue(String key, final T value, long time, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, time, unit);
    }

    /**
     * delete by key
     *
     * @param key key
     * @return delete flag
     */
    public static boolean deleteByKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
}
