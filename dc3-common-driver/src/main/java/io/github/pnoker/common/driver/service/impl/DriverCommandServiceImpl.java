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
import io.github.pnoker.common.driver.context.DriverContext;
import io.github.pnoker.common.driver.metadata.DeviceMetadata;
import io.github.pnoker.common.driver.metadata.DriverMetadata;
import io.github.pnoker.common.driver.metadata.PointMetadata;
import io.github.pnoker.common.driver.service.DriverCommandService;
import io.github.pnoker.common.driver.service.DriverCustomService;
import io.github.pnoker.common.driver.service.DriverSenderService;
import io.github.pnoker.common.entity.bo.AttributeBO;
import io.github.pnoker.common.entity.dto.*;
import io.github.pnoker.common.enums.AttributeTypeFlagEnum;
import io.github.pnoker.common.exception.ReadPointException;
import io.github.pnoker.common.exception.ServiceException;
import io.github.pnoker.common.utils.JsonUtil;
import io.github.pnoker.common.utils.ValueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverCommandServiceImpl implements DriverCommandService {

    private final DriverContext driverContext;
    private final DriverMetadata driverMetadata;
    private final DeviceMetadata deviceMetadata;
    private final PointMetadata pointMetadata;
    private final DriverSenderService driverSenderService;
    private final DriverCustomService driverCustomService;

    public DriverCommandServiceImpl(DriverContext driverContext, DriverMetadata driverMetadata, DeviceMetadata deviceMetadata, PointMetadata pointMetadata, DriverSenderService driverSenderService, DriverCustomService driverCustomService) {
        this.driverContext = driverContext;
        this.driverMetadata = driverMetadata;
        this.deviceMetadata = deviceMetadata;
        this.pointMetadata = pointMetadata;
        this.driverSenderService = driverSenderService;
        this.driverCustomService = driverCustomService;
    }

    @Override
    public PointValueDTO read(Long deviceId, Long pointId) {
        DeviceDTO device = deviceMetadata.getCache(deviceId);
        PointDTO point = pointMetadata.getCache(pointId);
        Map<Long, DriverAttributeConfigDTO> driverAttributeConfigMap = device.getDriverAttributeConfigMap();
        Map<Long, PointAttributeConfigDTO> pointAttributeConfigMap = device.getPointAttributeConfigMap();

        Map<String, AttributeBO> driverMap = new HashMap<>();
        for (Map.Entry<Long, DriverAttributeConfigDTO> entity : driverAttributeConfigMap.entrySet()) {
            DriverAttributeDTO driverAttributeDTO = driverMetadata.getDriverAttributeMap().get(entity.getKey());
            AttributeBO attributeBO = new AttributeBO();
            attributeBO.setType(driverAttributeDTO.getAttributeTypeFlag());
            attributeBO.setValue(entity.getValue().getConfigValue());
            driverMap.put(driverAttributeDTO.getAttributeName(), attributeBO);
        }

        Map<String, AttributeBO> pointMap = new HashMap<>();
        for (Map.Entry<Long, PointAttributeConfigDTO> entity : pointAttributeConfigMap.entrySet()) {
            PointAttributeDTO driverAttributeDTO = driverMetadata.getPointAttributeMap().get(entity.getKey());
            AttributeBO attributeBO = new AttributeBO();
            attributeBO.setType(driverAttributeDTO.getAttributeTypeFlag());
            attributeBO.setValue(entity.getValue().getConfigValue());
            pointMap.put(driverAttributeDTO.getAttributeName(), attributeBO);
        }


        try {
            String rawValue = driverCustomService.read(
                    driverMap,
                    pointMap,
                    device,
                    point
            );

            if (CharSequenceUtil.isEmpty(rawValue)) {
                throw new ReadPointException("The read point value is null");
            }
            if (DefaultConstant.DEFAULT_NULL_STRING_VALUE.equals(rawValue)) {
                throw new ReadPointException(CharSequenceUtil.format("The read point value is invalid: {}", rawValue));
            }

            PointValueDTO pointValue = new PointValueDTO(deviceId, pointId, rawValue, ValueUtil.getFinalValue(point, rawValue));
            driverSenderService.pointValueSender(pointValue);
            return pointValue;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void read(DeviceCommandDTO commandDTO) {
        DeviceCommandDTO.DeviceRead deviceRead = JsonUtil.parseObject(commandDTO.getContent(), DeviceCommandDTO.DeviceRead.class);
        if (ObjectUtil.isNull(deviceRead)) {
            return;
        }

        log.info("Start command of read: {}", JsonUtil.toJsonString(commandDTO));
        PointValueDTO read = read(deviceRead.getDeviceId(), deviceRead.getPointId());
        log.info("End command of read: {}", JsonUtil.toJsonString(read));
    }

    @Override
    public Boolean write(Long deviceId, Long pointId, String value) {
        DeviceDTO device = driverContext.getDeviceByDeviceId(deviceId);
        try {
            PointDTO point = driverContext.getPointByDeviceIdAndPointId(deviceId, pointId);
            AttributeTypeFlagEnum typeEnum = AttributeTypeFlagEnum.ofCode(point.getPointTypeFlag().getCode());
            return driverCustomService.write(
                    driverContext.getDriverConfigByDeviceId(deviceId),
                    driverContext.getPointConfigByDeviceIdAndPointId(deviceId, pointId),
                    device,
                    new AttributeBO(value, typeEnum)
            );
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void write(DeviceCommandDTO commandDTO) {
        DeviceCommandDTO.DeviceWrite deviceWrite = JsonUtil.parseObject(commandDTO.getContent(), DeviceCommandDTO.DeviceWrite.class);
        if (ObjectUtil.isNull(deviceWrite)) {
            return;
        }

        log.info("Start command of write: {}", JsonUtil.toJsonString(commandDTO));
        Boolean write = write(deviceWrite.getDeviceId(), deviceWrite.getPointId(), deviceWrite.getValue());
        log.info("End command of write: write {}", write);
    }

}
