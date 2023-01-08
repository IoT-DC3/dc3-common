/*
 * Copyright 2016-present Pnoker All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.pnoker.common.constant.common;

/**
 * 环境变量、配置变量 相关常量
 *
 * @author pnoker
 * @since 2022.1.0
 */
public class EnvironmentConstant {

    private EnvironmentConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 服务是否开启 Https
     */
    public static final String EUREKA_TLS_ENABLE = "eureka.client.tls.enabled";

    /**
     * Eureka 服务注册中心 Url
     */
    public static final String EUREKA_SERVICE_URL = "eureka.client.service-url.defaultZone";
}
