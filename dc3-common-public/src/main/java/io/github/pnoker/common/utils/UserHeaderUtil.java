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

import cn.hutool.core.thread.threadlocal.NamedThreadLocal;
import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.constant.common.ExceptionConstant;
import io.github.pnoker.common.entity.common.RequestHeader;
import io.github.pnoker.common.exception.UnAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

/**
 * 用户请求头 相关工具类
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
public class UserHeaderUtil {

    private UserHeaderUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    private static final ThreadLocal<RequestHeader.UserHeader> USER_HEADER_THREAD_LOCAL = new NamedThreadLocal<>("Request of user header");

    /**
     * 获取用户请求头信息
     *
     * @return {@link RequestHeader.UserHeader}
     */
    public static RequestHeader.UserHeader getUserHeader() {
        RequestHeader.UserHeader entityBO = USER_HEADER_THREAD_LOCAL.get();
        if (ObjectUtil.isNull(entityBO)) {
            throw new UnAuthorizedException("Unable to get auth info");
        }

        if (ObjectUtil.isNull(entityBO.getTenantId())) {
            throw new UnAuthorizedException("Unable to get tenant id of auth info");
        }

        if (ObjectUtil.isNull(entityBO.getUserId())) {
            throw new UnAuthorizedException("Unable to get user id of auth info");
        }

        return entityBO;
    }

    /**
     * 设置用户请求头信息
     *
     * @param entityBO {@link RequestHeader.UserHeader}
     */
    public static void setUserHeader(@Nullable RequestHeader.UserHeader entityBO) {
        if (ObjectUtil.isNull(entityBO)) {
            removeUserHeader();
        } else {
            USER_HEADER_THREAD_LOCAL.set(entityBO);
        }
    }

    /**
     * 清空用户请求头信息
     */
    public static void removeUserHeader() {
        USER_HEADER_THREAD_LOCAL.remove();
    }
}
