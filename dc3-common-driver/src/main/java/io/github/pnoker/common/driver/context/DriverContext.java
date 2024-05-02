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

package io.github.pnoker.common.driver.context;

import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.entity.dto.AttributeConfigDTO;
import io.github.pnoker.common.entity.dto.DeviceDTO;
import io.github.pnoker.common.entity.dto.DriverMetadataDTO;
import io.github.pnoker.common.entity.dto.PointDTO;
import io.github.pnoker.common.enums.DriverStatusEnum;
import io.github.pnoker.common.exception.NotFoundException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@Setter
@Slf4j
@Component
public class DriverContext {

    /**
     * 驱动 状态
     */
    private DriverStatusEnum driverStatus = DriverStatusEnum.OFFLINE;

    /**
     * 驱动 元数据, 当且仅当驱动注册成功之后由 Manager 返回
     */
    private DriverMetadataDTO driverMetadataDTO = new DriverMetadataDTO();

    /**
     * 根据 设备ID 获取连接设备的驱动配置信息
     *
     * @param deviceId 设备ID
     * @return Map String:AttributeInfo
     */
    public Map<String, AttributeConfigDTO> getDriverConfigByDeviceId(Long deviceId) {
        return this.driverMetadataDTO.getDriverConfigMap().get(deviceId);
    }

    /**
     * 根据 设备ID 获取连接设备的全部位号配置信息
     *
     * @param deviceId 设备ID
     * @return Map String:(Map String:AttributeInfo)
     */
    public Map<Long, Map<String, AttributeConfigDTO>> getPointConfigByDeviceId(Long deviceId) {
        Map<Long, Map<String, AttributeConfigDTO>> tmpMap = this.driverMetadataDTO.getPointConfigMap().get(deviceId);
        if (ObjectUtil.isNull(tmpMap) || tmpMap.size() < 1) {
            throw new NotFoundException("Device({}) does not exist", deviceId);
        }
        return tmpMap;
    }

    /**
     * 根据 设备ID 和 位号ID 获取连接设备的位号配置信息
     *
     * @param deviceId 设备ID
     * @param pointId  位号ID
     * @return Map String:AttributeInfo
     */
    public Map<String, AttributeConfigDTO> getPointConfigByDeviceIdAndPointId(Long deviceId, Long pointId) {
        Map<String, AttributeConfigDTO> tmpMap = getPointConfigByDeviceId(deviceId).get(pointId);
        if (ObjectUtil.isNull(tmpMap) || tmpMap.size() < 1) {
            throw new NotFoundException("Point({}) info does not exist", pointId);
        }
        return tmpMap;
    }

    /**
     * 根据 设备ID 获取设备
     *
     * @param deviceId 设备ID
     * @return Device
     */
    public DeviceDTO getDeviceByDeviceId(Long deviceId) {
        DeviceDTO device = this.driverMetadataDTO.getDeviceMap().get(deviceId);
        if (ObjectUtil.isNull(device)) {
            throw new NotFoundException("Device({}) does not exist", deviceId);
        }
        return device;
    }

    /**
     * 根据 设备ID 获取位号
     *
     * @param deviceId 设备ID
     * @return Point Array
     */
    public List<PointDTO> getPointByDeviceId(Long deviceId) {
        DeviceDTO device = getDeviceByDeviceId(deviceId);
        return this.driverMetadataDTO.getProfilePointMap().entrySet().stream()
                .filter(entry -> device.getProfileIds().contains(entry.getKey()))
                .map(entry -> new ArrayList<>(entry.getValue().values()))
                .reduce(new ArrayList<>(), (total, temp) -> {
                    total.addAll(temp);
                    return total;
                });
    }

    /**
     * 根据 设备ID和位号ID 获取位号
     *
     * @param deviceId 设备ID
     * @param pointId  位号ID
     * @return Point
     */
    public PointDTO getPointByDeviceIdAndPointId(Long deviceId, Long pointId) {
        DeviceDTO device = getDeviceByDeviceId(deviceId);
        Optional<Map<Long, PointDTO>> optional = this.driverMetadataDTO.getProfilePointMap().entrySet().stream()
                .filter(entry -> device.getProfileIds().contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .filter(entry -> entry.containsKey(pointId))
                .findFirst();

        if (optional.isPresent()) {
            return optional.get().get(pointId);
        }

        throw new NotFoundException("Point({}) info does not exist", pointId);
    }

}
