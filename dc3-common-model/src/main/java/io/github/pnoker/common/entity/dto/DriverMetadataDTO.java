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

package io.github.pnoker.common.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 驱动元数据
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@SuperBuilder
@AllArgsConstructor
public class DriverMetadataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long driverId;
    private Long tenantId;
    private Map<Long, DriverAttributeDTO> driverAttributeMap;
    private Map<Long, PointAttributeDTO> pointAttributeMap;

    /**
     * deviceId(driverAttribute.name,(driverInfo.value,driverAttribute.type))
     */
    private Map<Long, Map<String, AttributeInfoDTO>> driverInfoMap;

    /**
     * deviceId(pointId(pointAttribute.name,(pointInfo.value,pointAttribute.type)))
     */
    private Map<Long, Map<Long, Map<String, AttributeInfoDTO>>> pointInfoMap;

    /**
     * deviceId,device
     */
    private Map<Long, DeviceDTO> deviceMap;

    /**
     * profileId(pointId,point)
     */
    private Map<Long, Map<Long, PointDTO>> profilePointMap;

    public DriverMetadataDTO() {
        this.driverAttributeMap = new ConcurrentHashMap<>(16);
        this.pointAttributeMap = new ConcurrentHashMap<>(16);
        this.deviceMap = new ConcurrentHashMap<>(16);
        this.driverInfoMap = new ConcurrentHashMap<>(16);
        this.pointInfoMap = new ConcurrentHashMap<>(16);
        this.profilePointMap = new ConcurrentHashMap<>(16);
    }
}
