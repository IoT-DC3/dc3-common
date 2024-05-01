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
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.constant.driver.RabbitConstant;
import io.github.pnoker.common.driver.context.DriverContext;
import io.github.pnoker.common.driver.service.DriverSenderService;
import io.github.pnoker.common.driver.service.DriverSyncService;
import io.github.pnoker.common.entity.dto.DriverDTO;
import io.github.pnoker.common.entity.dto.DriverMetadataDTO;
import io.github.pnoker.common.entity.dto.DriverRegisterDTO;
import io.github.pnoker.common.entity.dto.DriverSyncDownDTO;
import io.github.pnoker.common.entity.property.DriverProperty;
import io.github.pnoker.common.enums.DriverStatusEnum;
import io.github.pnoker.common.utils.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 驱动同步相关接口实现
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverSyncServiceImpl implements DriverSyncService {

    @Resource
    private DriverContext driverContext;
    @Resource
    private DriverProperty driverProperty;

    @Resource
    private DriverSenderService driverSenderService;

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void up() {
        try {
            DriverRegisterDTO entityDTO = buildRegisterDTOByProperty();
            log.info("The driver {} is initializing", entityDTO.getClient());
            log.debug("The driver {} initialization information is: {}", driverProperty.getService(), JsonUtil.toJsonString(entityDTO));
            rabbitTemplate.convertAndSend(
                    RabbitConstant.TOPIC_EXCHANGE_REGISTER,
                    RabbitConstant.ROUTING_REGISTER_UP_PREFIX + driverProperty.getClient(),
                    entityDTO
            );

            threadPoolExecutor.submit(() -> {
                while (!DriverStatusEnum.ONLINE.equals(driverContext.getDriverStatus())) {
                    ThreadUtil.sleep(500);
                }
            }).get(15, TimeUnit.SECONDS);

            log.info("The driver {} is initialized successfully.", entityDTO.getClient());
        } catch (Exception ignored) {
            log.error("The driver initialization failed, registration response timed out.");
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }

    @Override
    public void down(DriverSyncDownDTO entityDTO) {
        if (ObjectUtil.isNull(entityDTO.getContent())) {
            return;
        }

        if (CharSequenceUtil.isEmpty(entityDTO.getContent())) {
            return;
        }
        DriverMetadataDTO driverMetadataDTO = JsonUtil.parseObject(entityDTO.getContent(), DriverMetadataDTO.class);
        if (ObjectUtil.isNull(driverMetadataDTO)) {
            driverMetadataDTO = new DriverMetadataDTO();
        }
        driverContext.setDriverMetadataDTO(driverMetadataDTO);
        driverContext.setDriverStatus(DriverStatusEnum.ONLINE);
        driverMetadataDTO.getDriverAttributeMap().values().forEach(driverAttribute -> log.info("Syncing driver attribute[{}] metadata: {}", driverAttribute.getAttributeName(), JsonUtil.toJsonString(driverAttribute)));
        driverMetadataDTO.getPointAttributeMap().values().forEach(pointAttribute -> log.info("Syncing point attribute[{}] metadata: {}", pointAttribute.getAttributeName(), JsonUtil.toJsonString(pointAttribute)));
        driverMetadataDTO.getDeviceMap().values().forEach(device -> log.info("Syncing device[{}] metadata: {}", device.getDeviceName(), JsonUtil.toJsonString(device)));
        log.info("The metadata synced successfully.");
    }

    /**
     * Property To DriverRegisterDTO
     *
     * @return DriverRegisterDTO
     */
    private DriverRegisterDTO buildRegisterDTOByProperty() {
        DriverRegisterDTO driverRegisterDTO = new DriverRegisterDTO();
        driverRegisterDTO.setDriver(buildDriverByProperty());
        driverRegisterDTO.setTenant(driverProperty.getTenant());
        driverRegisterDTO.setClient(driverProperty.getClient());
        driverRegisterDTO.setDriverAttributes(driverProperty.getDriverAttribute());
        driverRegisterDTO.setPointAttributes(driverProperty.getPointAttribute());
        return driverRegisterDTO;
    }

    /**
     * Property To Driver
     *
     * @return Driver
     */
    private DriverDTO buildDriverByProperty() {
        DriverDTO entityDO = new DriverDTO();
        entityDO.setDriverName(driverProperty.getName());
        entityDO.setDriverCode(driverProperty.getCode());
        entityDO.setServiceName(driverProperty.getService());
        entityDO.setServiceHost(driverProperty.getHost());
        entityDO.setDriverTypeFlag(driverProperty.getType());
        entityDO.setRemark(driverProperty.getRemark());
        return entityDO;
    }

}
