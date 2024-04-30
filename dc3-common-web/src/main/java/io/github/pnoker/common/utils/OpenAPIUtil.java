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

import io.github.pnoker.common.constant.common.ExceptionConstant;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;

/**
 * Swagger 相关工具类
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
public class OpenAPIUtil {

    private OpenAPIUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    public static OpenAPI getOpenAPI(String title) {
        return new OpenAPI().info(new Info()
                .title(title)
                .version("V3.0")
                .contact(new Contact().name("IoT DC3").url("https://gitee.com/pnoker/iot-dc3"))
                .description("IoT DC3 是一个基于 Spring Cloud 的 100% 完全开源的、分布式的物联网(IoT)平台, 用于快速开发物联网项目和管理物联设备, 是一整套物联系统解决方案。")
                .termsOfService("https://doc.dc3.site"));
    }
}
