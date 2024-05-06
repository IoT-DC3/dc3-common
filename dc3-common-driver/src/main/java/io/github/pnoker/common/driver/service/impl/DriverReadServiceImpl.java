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

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.constant.common.DefaultConstant;
import io.github.pnoker.common.driver.entity.bean.PointValue;
import io.github.pnoker.common.driver.entity.dto.DeviceDTO;
import io.github.pnoker.common.driver.entity.dto.PointDTO;
import io.github.pnoker.common.driver.metadata.DeviceMetadata;
import io.github.pnoker.common.driver.metadata.DriverMetadata;
import io.github.pnoker.common.driver.metadata.PointMetadata;
import io.github.pnoker.common.driver.service.DriverCustomService;
import io.github.pnoker.common.driver.service.DriverReadService;
import io.github.pnoker.common.driver.service.DriverSenderService;
import io.github.pnoker.common.entity.bo.AttributeBO;
import io.github.pnoker.common.entity.dto.DeviceCommandDTO;
import io.github.pnoker.common.exception.ReadPointException;
import io.github.pnoker.common.utils.JsonUtil;
import io.github.pnoker.common.utils.ValueUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverReadServiceImpl implements DriverReadService {

    @Resource
    private DriverMetadata driverMetadata;
    @Resource
    private DeviceMetadata deviceMetadata;
    @Resource
    private PointMetadata pointMetadata;
    @Resource
    private DriverSenderService driverSenderService;
    @Resource
    private DriverCustomService driverCustomService;

    @Override
    public void read(Long deviceId, Long pointId) {
        try {
            DeviceDTO device = deviceMetadata.getDevice(deviceId);
            if (ObjectUtil.isNull(device)) {
                throw new ReadPointException("Failed to read point value, device[{}] is null", deviceId);
            }

            if (!device.getPointIds().contains(pointId)) {
                throw new ReadPointException("Failed to read point value, device[{}] not contained point[{}]", deviceId, pointId);
            }

            Map<String, AttributeBO> driverConfig = deviceMetadata.getDriverAttributeConfig(deviceId);
            Map<String, AttributeBO> pointConfig = deviceMetadata.getPointAttributeConfig(deviceId,pointId);

            PointDTO point = pointMetadata.getPoint(pointId);
            if (ObjectUtil.isNull(point)) {
                throw new ReadPointException("Failed to read point value, point[{}] is null" + deviceId);
            }

            String rawValue = driverCustomService.read(driverConfig, pointConfig, device, point);
            if (CharSequenceUtil.isEmpty(rawValue)) {
                throw new ReadPointException("Failed to read point value, point value is null");
            }

            if (CharSequenceUtil.isNullOrUndefined(rawValue) || DefaultConstant.DEFAULT_NULL_STRING_VALUE.equals(rawValue)) {
                throw new ReadPointException(CharSequenceUtil.format("Failed to read point value, point value is invalid: {}", rawValue));
            }

            PointValue pointValue = new PointValue(deviceId, pointId, rawValue, ValueUtil.getValue(point, rawValue));
            driverSenderService.pointValueSender(pointValue);
        } catch (Exception e) {
            throw new ReadPointException(e.getMessage(), e);
        }
    }

    @Override
    public void read(DeviceCommandDTO commandDTO) {
        DeviceCommandDTO.DeviceRead deviceRead = JsonUtil.parseObject(commandDTO.getContent(), DeviceCommandDTO.DeviceRead.class);
        if (ObjectUtil.isNull(deviceRead)) {
            return;
        }

        log.info("Start command of read: {}", JsonUtil.toJsonString(commandDTO));
        read(deviceRead.getDeviceId(), deviceRead.getPointId());
        log.info("End command of read");
    }

}
