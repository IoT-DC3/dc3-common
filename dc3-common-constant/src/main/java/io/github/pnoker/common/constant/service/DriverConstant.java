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

package io.github.pnoker.common.constant.service;

import io.github.pnoker.common.constant.common.ExceptionConstant;

/**
 * 驱动服务 相关常量
 *
 * @author pnoker
 * @since 2022.1.0
 */
public class DriverConstant {

    /**
     * 服务名
     */
    public static final String SERVICE_NAME = "dc3-driver";
    public static final String COMMAND_URL_PREFIX = "/command";

    private DriverConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }
}
