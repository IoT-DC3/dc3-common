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
 * @since 2023.04.11
 */
public class AuthCacheUtil {

    private AuthCacheUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    private static final RedisTemplate redisTemplate = SpringUtil.getBean(RedisTemplate.class);

    /**
     * 获取Redis User key
     *
     * @param suffix   类型，SuffixConstant
     * @param tenantId 租户id
     * @param userName 用户名称
     * @return key
     */
    private static String getKey(String suffix, String tenantId, String userName) {
        return PrefixConstant.USER + suffix + SymbolConstant.DOUBLE_COLON + userName +
                SymbolConstant.HASHTAG + tenantId;
    }

    /**
     * 获取 用户 - token 映射Key
     *
     * @param tenantId tenantId
     * @param userName userName
     * @return key
     */
    public static String getUserTokenKey(String tenantId, String userName) {
        return getKey(SuffixConstant.TOKEN, tenantId, userName);
    }


    /**
     * 获取Redis Token Key
     *
     * @param token token
     * @return token key
     */
    public static String getTokenKey(String token) {
        return AuthConstant.redis_prefix + SymbolConstant.DOUBLE_COLON + token;
    }

    /**
     * 获取Salt key
     *
     * @param tenantId 租户id
     * @param userName 用户名称
     * @return Salt key
     */
    public static String getSaltKey(String tenantId, String userName) {
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
