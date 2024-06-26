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


import cn.hutool.core.text.CharSequenceUtil;
import io.github.pnoker.common.constant.AuthConstant;
import io.github.pnoker.common.constant.cache.TimeoutConstant;
import io.github.pnoker.common.exception.ServiceException;
import io.github.pnoker.common.exception.UnAuthorizedException;
import io.github.pnoker.common.model.AuthUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author linys
 * @since 2022.1.0
 */
public class AuthUtil {

    /**
     * get salt
     *
     * @param tenantId 租户ID
     * @param userName 用户名
     * @return salt
     */
    public static String getPasswordSalt(Long tenantId, String userName) {
        String saltKey = AuthCacheUtil.getSaltKey(tenantId, userName);
        if (Objects.isNull(saltKey)) {
            return null;
        }

        return AuthCacheUtil.getValue(saltKey);
    }

    /**
     * create token and save to cache
     *
     * @param tenantId 租户ID
     * @param userName 用户名
     * @param salt     盐值
     * @return token
     */
    public static String createToken(Long tenantId, String userName, String salt) {
        String tokenKey = AuthCacheUtil.getUserTokenKey(tenantId, userName);
        String token = AuthCacheUtil.getValue(tokenKey);
        if (CharSequenceUtil.isEmpty(token)) {
            token = KeyUtil.generateToken(userName, salt, tenantId);
        }
        AuthCacheUtil.setValue(tokenKey, token, TimeoutConstant.TOKEN_CACHE_TIMEOUT, TimeUnit.HOURS);
        return token;
    }

    /**
     * get login user's token
     *
     * @return token
     */
    public static String getLoginToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            throw new ServiceException("requestAttributes can't be null!");
        }

        String token = requestAttributes.getRequest().getHeader(AuthConstant.header_token);
        if (CharSequenceUtil.isBlank(token)) {
            throw new ServiceException("please login first!");
        }

        return token;
    }

    /**
     * check login status
     *
     * @return true if login
     */
    public static boolean checkLogin() {
        try {
            getLoginToken();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static AuthUser getAuthUser() {
        String token = getLoginToken();
        AuthUser authUser = getAuthUserByToken(token);
        if (Objects.isNull(authUser)) {
            throw new UnAuthorizedException("please login first!");
        }
        return authUser;
    }

    /**
     * get login user id
     *
     * @return user id
     */
    public static Long getLoginUserId() {
        return getAuthUser().getUserId();
    }

    /**
     * save token to AuthUser map into redis
     *
     * @param token    token
     * @param authUser AuthUser
     */
    public static void saveTokenToAuthUserMap(String token, AuthUser authUser) {
        String tokenKey = AuthCacheUtil.getTokenKey(token);
        AuthCacheUtil.setValue(tokenKey, authUser, TimeoutConstant.TOKEN_CACHE_TIMEOUT, TimeUnit.HOURS);
    }

    /**
     * get AuthUser by token
     *
     * @param token token
     * @return AuthUser
     */
    public static AuthUser getAuthUserByToken(String token) {
        String tokenKey = AuthCacheUtil.getTokenKey(token);
        return AuthCacheUtil.getValue(tokenKey);
    }

    /**
     * logout
     */
    public static void logout() {
        String token = getLoginToken();
        AuthUser authUser = getAuthUserByToken(token);

        String userTokenKey = AuthCacheUtil.getUserTokenKey(authUser.getTenantId(), authUser.getUserName());
        AuthCacheUtil.deleteByKey(userTokenKey);

        String tokenKey = AuthCacheUtil.getTokenKey(token);
        AuthCacheUtil.deleteByKey(tokenKey);
    }
}
