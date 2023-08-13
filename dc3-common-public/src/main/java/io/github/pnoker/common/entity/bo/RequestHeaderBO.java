/*
 * Copyright 2016-present the original author or authors.
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

package io.github.pnoker.common.entity.bo;

import io.github.pnoker.common.constant.common.ExceptionConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request Header BO
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
public class RequestHeaderBO {

    private RequestHeaderBO() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenHeader {
        /**
         * 盐值
         */
        private String salt;

        /**
         * JWT Token
         */
        private String token;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserHeader {

        /**
         * 用户 ID
         */
        private String userId;

        /**
         * 用户别名
         */
        private String nickName;

        /**
         * 用户名称
         */
        private String userName;

        /**
         * 租户ID
         */
        private String tenantId;
    }

}