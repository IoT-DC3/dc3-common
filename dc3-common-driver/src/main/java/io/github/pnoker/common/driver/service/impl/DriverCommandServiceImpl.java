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
import io.github.pnoker.common.driver.service.DriverCommandService;
import io.github.pnoker.common.driver.service.DriverCustomService;
import io.github.pnoker.common.driver.service.DriverSenderService;
import io.github.pnoker.common.entity.dto.*;
import io.github.pnoker.common.enums.AttributeTypeFlagEnum;
import io.github.pnoker.common.exception.ReadPointException;
import io.github.pnoker.common.exception.ServiceException;
import io.github.pnoker.common.utils.ConvertUtil;
import io.github.pnoker.common.utils.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverCommandServiceImpl implements DriverCommandService {

    @Resource
    private DriverContext driverContext;
    @Resource
    private DriverSenderService driverSenderService;
    @Resource
    private DriverCustomService driverCustomService;

    @Override
    public PointValueDTO read(Long deviceId, Long pointId) {
        DeviceDTO device = driverContext.getDeviceByDeviceId(deviceId);
        PointDTO point = driverContext.getPointByDeviceIdAndPointId(deviceId, pointId);

        try {
            String rawValue = driverCustomService.read(
                    driverContext.getDriverConfigByDeviceId(deviceId),
                    driverContext.getPointConfigByDeviceIdAndPointId(deviceId, pointId),
                    device,
                    driverContext.getPointByDeviceIdAndPointId(deviceId, pointId)
            );

            if (CharSequenceUtil.isEmpty(rawValue)) {
                throw new ReadPointException("The read point value is null");
            }
            if (DefaultConstant.DEFAULT_NULL_STRING_VALUE.equals(rawValue)) {
                throw new ReadPointException(CharSequenceUtil.format("The read point value is invalid: {}", rawValue));
            }

            PointValueDTO pointValue = new PointValueDTO(deviceId, pointId, rawValue, ConvertUtil.convertValue(point, rawValue));
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
                    new AttributeConfigDTO(value, typeEnum)
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
