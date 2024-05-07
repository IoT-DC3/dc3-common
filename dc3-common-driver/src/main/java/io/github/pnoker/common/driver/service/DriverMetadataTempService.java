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

package io.github.pnoker.common.driver.service;

import io.github.pnoker.common.driver.entity.bo.DeviceBO;
import io.github.pnoker.common.driver.entity.bo.PointBO;

/**
 * @author pnoker
 * @since 2022.1.0
 */
public interface DriverMetadataTempService {

    /**
     * 向驱动原数据中添加设备
     *
     * @param device Device
     */
    void upsertDevice(DeviceBO device);

    /**
     * 删除驱动原数据中设备
     *
     * @param id ID
     */
    void deleteDevice(Long id);

    /**
     * 向驱动原数据中添加位号
     *
     * @param point Point
     */
    void upsertPoint(PointBO point);

    /**
     * 删除驱动原数据中位号
     *
     * @param id ID
     */
    void deletePoint(Long id);
}
