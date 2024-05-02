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
import io.github.pnoker.common.constant.driver.RabbitConstant;
import io.github.pnoker.common.driver.entity.property.DriverProperty;
import io.github.pnoker.common.driver.service.DriverSenderService;
import io.github.pnoker.common.entity.dto.DeviceEventDTO;
import io.github.pnoker.common.entity.dto.DriverEventDTO;
import io.github.pnoker.common.entity.dto.PointValueDTO;
import io.github.pnoker.common.enums.DeviceEventTypeEnum;
import io.github.pnoker.common.enums.DeviceStatusEnum;
import io.github.pnoker.common.utils.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverSenderServiceImpl implements DriverSenderService {

    @Resource
    private DriverProperty driverProperty;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void driverEventSender(DriverEventDTO entityDTO) {
        if (ObjectUtil.isNull(entityDTO)) {
            return;
        }

        rabbitTemplate.convertAndSend(
                RabbitConstant.TOPIC_EXCHANGE_EVENT,
                RabbitConstant.ROUTING_DRIVER_EVENT_PREFIX + driverProperty.getService(),
                entityDTO
        );
    }

    @Override
    public void deviceEventSender(DeviceEventDTO entityDTO) {
        if (!ObjectUtil.isNotNull(entityDTO)) {
            return;
        }

        rabbitTemplate.convertAndSend(
                RabbitConstant.TOPIC_EXCHANGE_EVENT,
                RabbitConstant.ROUTING_DEVICE_EVENT_PREFIX + driverProperty.getService(),
                entityDTO
        );
    }

    @Override
    public void deviceStatusSender(Long deviceId, DeviceStatusEnum status) {
        sendDeviceStatus(deviceId, status, 15, TimeUnit.MINUTES);
    }

    @Override
    public void deviceStatusSender(Long deviceId, DeviceStatusEnum status, int timeOut, TimeUnit timeUnit) {
        sendDeviceStatus(deviceId, status, timeOut, timeUnit);
    }

    @Override
    public void pointValueSender(PointValueDTO entityDTO) {
        if (ObjectUtil.isNotNull(entityDTO)) {
            log.info("Send point value: {}", JsonUtil.toJsonString(entityDTO));
            rabbitTemplate.convertAndSend(
                    RabbitConstant.TOPIC_EXCHANGE_VALUE,
                    RabbitConstant.ROUTING_POINT_VALUE_PREFIX + driverProperty.getService(),
                    entityDTO
            );
        }
    }

    @Override
    public void pointValueSender(List<PointValueDTO> entityDTOS) {
        if (ObjectUtil.isNotNull(entityDTOS)) {
            entityDTOS.forEach(this::pointValueSender);
        }
    }

    private void sendDeviceStatus(Long deviceId, DeviceStatusEnum status, int timeOut, TimeUnit timeUnit) {
        DeviceEventDTO.DeviceStatus deviceStatus = new DeviceEventDTO.DeviceStatus(deviceId, status, timeOut, timeUnit);
        DeviceEventDTO deviceEventDTO = new DeviceEventDTO(DeviceEventTypeEnum.HEARTBEAT, JsonUtil.toJsonString(deviceStatus));
        log.info("Report device event: {}, event content: {}", deviceEventDTO.getType().getCode(), JsonUtil.toJsonString(deviceEventDTO));
        deviceEventSender(deviceEventDTO);
    }

}
