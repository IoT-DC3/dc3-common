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

import io.github.pnoker.common.driver.entity.dto.DriverDTO;
import io.github.pnoker.common.driver.entity.dto.DriverRegisterDTO;
import io.github.pnoker.common.driver.entity.property.DriverProperty;
import io.github.pnoker.common.driver.grpc.client.DeviceClient;
import io.github.pnoker.common.driver.grpc.client.DriverClient;
import io.github.pnoker.common.driver.grpc.client.PointClient;
import io.github.pnoker.common.driver.metadata.DeviceMetadata;
import io.github.pnoker.common.driver.metadata.PointMetadata;
import io.github.pnoker.common.driver.service.DriverRegisterService;
import io.github.pnoker.common.enums.DriverTypeFlagEnum;
import io.github.pnoker.common.utils.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 驱动注册相关接口实现
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverRegisterServiceImpl implements DriverRegisterService {

    @Resource
    private DriverProperty driverProperty;

    @Resource
    private DriverClient driverClient;
    @Resource
    private DeviceClient deviceClient;
    @Resource
    private PointClient pointClient;
    @Resource
    private DeviceMetadata deviceMetadata;
    @Resource
    private PointMetadata pointMetadata;

    @Override
    public void initial() {
        try {
            DriverRegisterDTO entityDTO = buildRegisterDTOByProperty();
            log.info("The driver information is: {}", JsonUtil.toJsonString(entityDTO));
            driverClient.driverRegister(entityDTO);
            if (DriverTypeFlagEnum.DRIVER_CLIENT.equals(entityDTO.getDriver().getDriverTypeFlag())) {
                deviceMetadata.loadAllCache();
                pointMetadata.loadAllCache();
            }
        } catch (Exception e) {
            log.error("Driver initialization failed: {}", e.getMessage(), e);
            System.exit(1);
        }
    }

    /**
     * 构建驱动注册信息
     *
     * @return DriverRegisterDTO
     */
    private DriverRegisterDTO buildRegisterDTOByProperty() {
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setDriverName(driverProperty.getName());
        driverDTO.setDriverCode(driverProperty.getCode());
        driverDTO.setServiceName(driverProperty.getService());
        driverDTO.setServiceHost(driverProperty.getHost());
        driverDTO.setDriverTypeFlag(driverProperty.getType());
        driverDTO.setRemark(driverProperty.getRemark());

        DriverRegisterDTO driverRegisterDTO = new DriverRegisterDTO();
        driverRegisterDTO.setDriver(driverDTO);
        driverRegisterDTO.setTenant(driverProperty.getTenant());
        driverRegisterDTO.setClient(driverProperty.getClient());
        driverRegisterDTO.setDriverAttributes(driverProperty.getDriverAttribute());
        driverRegisterDTO.setPointAttributes(driverProperty.getPointAttribute());
        return driverRegisterDTO;
    }

}