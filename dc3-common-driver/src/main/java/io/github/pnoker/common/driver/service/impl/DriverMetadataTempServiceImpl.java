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

package io.github.pnoker.common.driver.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.driver.context.DriverContext;
import io.github.pnoker.common.driver.service.DriverMetadataTempService;
import io.github.pnoker.common.entity.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Driver Metadata Service Implements
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverMetadataTempServiceImpl implements DriverMetadataTempService {

    @Resource
    private DriverContext driverContext;

    @Override
    public void upsertProfile(ProfileDTO profile) {
        // Add profile point to context
        driverContext.getDriverMetadataDTO().getProfilePointMap().computeIfAbsent(profile.getId(), k -> new ConcurrentHashMap<>(16));
    }

    @Override
    public void deleteProfile(Long id) {
        driverContext.getDriverMetadataDTO().getProfilePointMap().entrySet().removeIf(next -> next.getKey().equals(id));
    }

    @Override
    public void upsertDevice(DeviceDTO device) {
        // Add device to context
        driverContext.getDriverMetadataDTO().getDeviceMap().put(device.getId(), device);
        // Add device driver attribute config to context
        driverContext.getDriverMetadataDTO().getDriverInfoMap().computeIfAbsent(device.getId(), k -> new ConcurrentHashMap<>(16));
        // Add device point attribute config to context
        driverContext.getDriverMetadataDTO().getPointInfoMap().computeIfAbsent(device.getId(), k -> new ConcurrentHashMap<>(16));
    }

    @Override
    public void deleteDevice(Long id) {
        driverContext.getDriverMetadataDTO().getDeviceMap().entrySet().removeIf(next -> next.getKey().equals(id));
        driverContext.getDriverMetadataDTO().getDriverInfoMap().entrySet().removeIf(next -> next.getKey().equals(id));
        driverContext.getDriverMetadataDTO().getPointInfoMap().entrySet().removeIf(next -> next.getKey().equals(id));
    }

    @Override
    public void upsertPoint(PointDTO point) {
        // Upsert point to profile point map context
        driverContext.getDriverMetadataDTO().getProfilePointMap().computeIfAbsent(point.getProfileId(), k -> new ConcurrentHashMap<>(16)).put(point.getId(), point);
    }

    @Override
    public void deletePoint(Long profileId, Long pointId) {
        // Delete point from profile point map context
        driverContext.getDriverMetadataDTO().getProfilePointMap().computeIfPresent(profileId, (k, v) -> {
            v.entrySet().removeIf(next -> next.getKey().equals(pointId));
            return v;
        });
    }

    @Override
    public void upsertDriverInfo(DriverAttributeConfigDTO driverAttributeConfig) {
        DriverAttributeDTO attribute = driverContext.getDriverMetadataDTO().getDriverAttributeMap().get(driverAttributeConfig.getDriverAttributeId());
        if (ObjectUtil.isNotNull(attribute)) {
            // Add driver attribute config to driver attribute config map context
            driverContext.getDriverMetadataDTO().getDriverInfoMap().computeIfAbsent(driverAttributeConfig.getDeviceId(), k -> new ConcurrentHashMap<>(16))
                    .put(attribute.getAttributeName(), new AttributeConfigDTO(driverAttributeConfig.getConfigValue(), attribute.getAttributeTypeFlag()));
        }
    }

    @Override
    public void deleteDriverInfo(Long deviceId, Long attributeId) {
        DriverAttributeDTO attribute = driverContext.getDriverMetadataDTO().getDriverAttributeMap().get(attributeId);
        if (ObjectUtil.isNotNull(attribute)) {
            // Delete driver attribute config from driver attribute config map context
            driverContext.getDriverMetadataDTO().getDriverInfoMap().computeIfPresent(deviceId, (k, v) -> {
                v.entrySet().removeIf(next -> next.getKey().equals(attribute.getAttributeName()));
                return v;
            });

            // If the driver attribute is null, delete the driver attribute config from the driver attribute config map context
            driverContext.getDriverMetadataDTO().getDriverInfoMap().entrySet().removeIf(next -> next.getValue().size() < 1);
        }
    }

    @Override
    public void upsertPointInfo(PointAttributeConfigDTO pointAttributeConfig) {
        PointAttributeDTO attribute = driverContext.getDriverMetadataDTO().getPointAttributeMap().get(pointAttributeConfig.getPointAttributeId());
        if (ObjectUtil.isNotNull(attribute)) {
            // Add the point attribute config to the device point attribute config map context
            driverContext.getDriverMetadataDTO().getPointInfoMap().computeIfAbsent(pointAttributeConfig.getDeviceId(), k -> new ConcurrentHashMap<>(16))
                    .computeIfAbsent(pointAttributeConfig.getPointId(), k -> new ConcurrentHashMap<>(16))
                    .put(attribute.getAttributeName(), new AttributeConfigDTO(pointAttributeConfig.getConfigValue(), attribute.getAttributeTypeFlag()));
        }
    }

    @Override
    public void deletePointInfo(Long deviceId, Long pointId, Long attributeId) {
        PointAttributeDTO attribute = driverContext.getDriverMetadataDTO().getPointAttributeMap().get(attributeId);
        if (ObjectUtil.isNotNull(attribute)) {
            // Delete the point attribute config from the device info map context
            driverContext.getDriverMetadataDTO().getPointInfoMap().computeIfPresent(deviceId, (key1, value1) -> {
                value1.computeIfPresent(pointId, (key2, value2) -> {
                    value2.entrySet().removeIf(next -> next.getKey().equals(attribute.getAttributeName()));
                    return value2;
                });
                return value1;
            });

            // If the point attribute is null, delete the point attribute config from the point attribute config map context
            driverContext.getDriverMetadataDTO().getPointInfoMap().computeIfPresent(deviceId, (key, value) -> {
                value.entrySet().removeIf(next -> next.getValue().size() < 1);
                return value;
            });
        }
    }

}
