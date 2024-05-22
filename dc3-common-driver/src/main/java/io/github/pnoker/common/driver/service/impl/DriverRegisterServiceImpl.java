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

import io.github.pnoker.common.driver.entity.bo.DriverBO;
import io.github.pnoker.common.driver.entity.bo.DriverRegisterBO;
import io.github.pnoker.common.driver.entity.property.DriverProperty;
import io.github.pnoker.common.driver.grpc.client.DriverClient;
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
    private DeviceMetadata deviceMetadata;
    @Resource
    private PointMetadata pointMetadata;

    @Override
    public void initial() {
        try {
            DriverRegisterBO entityBO = buildRegisterBOByProperty();
            log.info("The driver information is: {}", JsonUtil.toJsonString(entityBO));
            driverClient.driverRegister(entityBO);

            // 根据驱动类型决定是否加载该驱动到设备和位号数据到本地缓存
            if (DriverTypeFlagEnum.DRIVER_CLIENT.equals(entityBO.getDriver().getDriverTypeFlag())) {
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
     * @return DriverRegisterBO
     */
    private DriverRegisterBO buildRegisterBOByProperty() {
        DriverBO driverBO = new DriverBO();
        driverBO.setDriverName(driverProperty.getName());
        driverBO.setDriverCode(driverProperty.getCode());
        driverBO.setServiceName(driverProperty.getService());
        driverBO.setServiceHost(driverProperty.getHost());
        driverBO.setDriverTypeFlag(driverProperty.getType());
        driverBO.setRemark(driverProperty.getRemark());

        DriverRegisterBO driverRegisterBO = new DriverRegisterBO();
        driverRegisterBO.setDriver(driverBO);
        driverRegisterBO.setTenant(driverProperty.getTenant());
        driverRegisterBO.setClient(driverProperty.getClient());
        driverRegisterBO.setDriverAttributes(driverProperty.getDriverAttribute());
        driverRegisterBO.setPointAttributes(driverProperty.getPointAttribute());
        return driverRegisterBO;
    }

}
