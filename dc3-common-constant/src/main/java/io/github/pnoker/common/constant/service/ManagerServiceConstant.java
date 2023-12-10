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
 * 管理服务 相关常量
 *
 * @author pnoker
 * @since 2022.1.0
 */
public class ManagerServiceConstant {

    private ManagerServiceConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 服务名
     */
    public static final String SERVICE_NAME = "dc3-center-manager";

    public static final String INDEX_URL_PREFIX = "/manager";
    public static final String DRIVER_URL_PREFIX = "/manager/driver";
    public static final String BATCH_URL_PREFIX = "/manager/batch";
    public static final String DRIVER_ATTRIBUTE_URL_PREFIX = "/manager/driver_attribute";
    public static final String POINT_ATTRIBUTE_URL_PREFIX = "/manager/point_attribute";
    public static final String PROFILE_URL_PREFIX = "/manager/profile";
    public static final String POINT_URL_PREFIX = "/manager/point";
    public static final String GROUP_URL_PREFIX = "/manager/group";
    public static final String DEVICE_URL_PREFIX = "/manager/device";
    public static final String AUTO_URL_PREFIX = "/manager/auto";
    public static final String POINT_ATTRIBUTE_CONFIG_URL_PREFIX = "/manager/point_attribute_config";
    public static final String DRIVER_ATTRIBUTE_CONFIG_URL_PREFIX = "/manager/driver_attribute_config";
    public static final String LABEL_URL_PREFIX = "/manager/label";
    public static final String DICTIONARY_URL_PREFIX = "/manager/dictionary";

}
