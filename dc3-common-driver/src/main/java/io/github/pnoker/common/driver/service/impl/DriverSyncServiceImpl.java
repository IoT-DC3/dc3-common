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
import io.github.pnoker.common.driver.context.DriverContext;
import io.github.pnoker.common.driver.entity.property.DriverProperty;
import io.github.pnoker.common.driver.grpc.client.DeviceClient;
import io.github.pnoker.common.driver.grpc.client.DriverClient;
import io.github.pnoker.common.driver.grpc.client.PointClient;
import io.github.pnoker.common.driver.metadata.DeviceMetadata;
import io.github.pnoker.common.driver.metadata.DriverMetadata;
import io.github.pnoker.common.driver.metadata.PointMetadata;
import io.github.pnoker.common.driver.service.DriverSyncService;
import io.github.pnoker.common.entity.dto.*;
import io.github.pnoker.common.enums.DriverStatusEnum;
import io.github.pnoker.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 驱动同步相关接口实现
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverSyncServiceImpl implements DriverSyncService {

    private final DriverClient driverClient;
    private final DeviceClient deviceClient;
    private final PointClient pointClient;
    private final DriverContext driverContext;
    private final DriverMetadata driverMetadata;
    private final DeviceMetadata deviceMetadata;
    private final PointMetadata pointMetadata;
    private final DriverProperty driverProperty;
    private final RabbitTemplate rabbitTemplate;
    private final ThreadPoolExecutor threadPoolExecutor;

    public DriverSyncServiceImpl(DriverClient driverClient, DeviceClient deviceClient, PointClient pointClient, DriverContext driverContext,
                                 DriverMetadata driverMetadata, DeviceMetadata deviceMetadata, PointMetadata pointMetadata, DriverProperty driverProperty,
                                 RabbitTemplate rabbitTemplate, ThreadPoolExecutor threadPoolExecutor) {
        this.driverClient = driverClient;
        this.pointClient = pointClient;
        this.deviceClient = deviceClient;
        this.driverContext = driverContext;
        this.driverMetadata = driverMetadata;
        this.deviceMetadata = deviceMetadata;
        this.pointMetadata = pointMetadata;
        this.driverProperty = driverProperty;
        this.rabbitTemplate = rabbitTemplate;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void up() {
        try {
            DriverRegisterDTO entityDTO = buildRegisterDTOByProperty();
            log.info("The driver {} is initializing", entityDTO.getClient());
            log.debug("The driver {} initialization information is: {}", driverProperty.getService(), JsonUtil.toJsonString(entityDTO));
            driverClient.driverRegister(entityDTO);
            List<DeviceDTO> deviceDTOList = deviceClient.list();
            deviceDTOList.forEach(deviceDTO -> deviceMetadata.setCache(deviceDTO.getId(), deviceDTO));
            List<PointDTO> pointDTOList = pointClient.list();
            pointDTOList.forEach(pointDTO -> pointMetadata.setCache(pointDTO.getId(), pointDTO));
            log.info("The driver {} is initialized successfully.", entityDTO.getClient());
        } catch (Exception e) {
            log.error("The driver initialization failed: {}", e.getMessage(), e);
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
        driverContext.setDriverMetadata(driverMetadataDTO);
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
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setDriverName(driverProperty.getName());
        driverDTO.setDriverCode(driverProperty.getCode());
        driverDTO.setServiceName(driverProperty.getService());
        driverDTO.setServiceHost(driverProperty.getHost());
        driverDTO.setDriverTypeFlag(driverProperty.getType());
        driverDTO.setRemark(driverProperty.getRemark());
        driverRegisterDTO.setDriver(driverDTO);
        driverRegisterDTO.setTenant(driverProperty.getTenant());
        driverRegisterDTO.setClient(driverProperty.getClient());
        driverRegisterDTO.setDriverAttributes(driverProperty.getDriverAttribute());
        driverRegisterDTO.setPointAttributes(driverProperty.getPointAttribute());
        return driverRegisterDTO;
    }

}
