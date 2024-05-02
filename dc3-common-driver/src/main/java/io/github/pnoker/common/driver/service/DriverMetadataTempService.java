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

import io.github.pnoker.common.entity.dto.*;

/**
 * @author pnoker
 * @since 2022.1.0
 */
public interface DriverMetadataTempService {

    /**
     * 向 DeviceDriver 中添加模板
     *
     * @param profile Profile
     */
    void upsertProfile(ProfileDTO profile);

    /**
     * 删除驱动原数据中模板
     *
     * @param id ID
     */
    void deleteProfile(Long id);

    /**
     * 向驱动原数据中添加设备
     *
     * @param device Device
     */
    void upsertDevice(DeviceDTO device);

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
    void upsertPoint(PointDTO point);

    /**
     * 删除驱动原数据中位号
     *
     * @param profileId 模板ID
     * @param id        ID
     */
    void deletePoint(Long profileId, Long id);

    /**
     * 向驱动原数据中添加驱动配置信息
     *
     * @param driverAttributeConfig DriverConfig
     */
    void upsertDriverConfig(DriverAttributeConfigDTO driverAttributeConfig);

    /**
     * 删除驱动原数据中添加驱动配置信息
     *
     * @param deviceId    设备ID
     * @param attributeId Attribute ID
     */
    void deleteDriverConfig(Long deviceId, Long attributeId);

    /**
     * 向驱动原数据中添加位号配置信息
     *
     * @param pointAttributeConfig PointConfig
     */
    void upsertPointConfig(PointAttributeConfigDTO pointAttributeConfig);

    /**
     * 删除驱动原数据中添加位号配置信息
     *
     * @param deviceId    设备ID
     * @param pointId     位号ID
     * @param attributeId Attribute ID
     */
    void deletePointConfig(Long deviceId, Long pointId, Long attributeId);
}
