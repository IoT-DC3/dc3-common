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

import io.github.pnoker.common.driver.service.DriverMetadataService;
import io.github.pnoker.common.driver.service.DriverMetadataTempService;
import io.github.pnoker.common.entity.dto.*;
import io.github.pnoker.common.enums.MetadataCommandTypeEnum;
import io.github.pnoker.common.utils.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 驱动元数据相关接口实现
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverMetadataServiceImpl implements DriverMetadataService {

    @Resource
    private DriverMetadataTempService driverMetadataTempService;

    @Override
    public void profileMetadata(DriverTransferMetadataDTO entityDTO) {
        ProfileDTO profile = JsonUtil.parseObject(entityDTO.getContent(), ProfileDTO.class);
        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Upsert profile: {}", JsonUtil.toJsonString(profile));
            driverMetadataTempService.upsertProfile(profile);
        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Delete profile: {}", JsonUtil.toJsonString(profile));
            driverMetadataTempService.deleteProfile(profile.getId());
        }
    }

    @Override
    public void deviceMetadata(DriverTransferMetadataDTO entityDTO) {
        DeviceDTO device = JsonUtil.parseObject(entityDTO.getContent(), DeviceDTO.class);
        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Upsert device: {}", JsonUtil.toJsonString(device));
            driverMetadataTempService.upsertDevice(device);
        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Delete device: {}", JsonUtil.toJsonString(device));
            driverMetadataTempService.deleteDevice(device.getId());
        }
    }

    @Override
    public void pointMetadata(DriverTransferMetadataDTO entityDTO) {
        PointDTO point = JsonUtil.parseObject(entityDTO.getContent(), PointDTO.class);
        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Upsert point: {}", JsonUtil.toJsonString(point));
            driverMetadataTempService.upsertPoint(point);
        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Delete point: {}", JsonUtil.toJsonString(point));
            driverMetadataTempService.deletePoint(point.getProfileId(), point.getId());
        }
    }

    @Override
    public void driverConfigMetadata(DriverTransferMetadataDTO entityDTO) {
        DriverAttributeConfigDTO driverAttributeConfig = JsonUtil.parseObject(entityDTO.getContent(), DriverAttributeConfigDTO.class);
        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Upsert driver attribute config: {}", JsonUtil.toJsonString(driverAttributeConfig));
            driverMetadataTempService.upsertDriverConfig(driverAttributeConfig);
        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Delete driver attribute config: {}", JsonUtil.toJsonString(driverAttributeConfig));
            driverMetadataTempService.deleteDriverConfig(driverAttributeConfig.getDeviceId(), driverAttributeConfig.getDriverAttributeId());
        }
    }

    @Override
    public void pointConfigMetadata(DriverTransferMetadataDTO entityDTO) {
        PointAttributeConfigDTO pointAttributeConfig = JsonUtil.parseObject(entityDTO.getContent(), PointAttributeConfigDTO.class);
        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Upsert point attribute config: {}", JsonUtil.toJsonString(pointAttributeConfig));
            driverMetadataTempService.upsertPointConfig(pointAttributeConfig);
        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Delete point attribute config: {}", JsonUtil.toJsonString(pointAttributeConfig));
            driverMetadataTempService.deletePointConfig(pointAttributeConfig.getPointId(), pointAttributeConfig.getId(), pointAttributeConfig.getPointAttributeId());
        }
    }
}
