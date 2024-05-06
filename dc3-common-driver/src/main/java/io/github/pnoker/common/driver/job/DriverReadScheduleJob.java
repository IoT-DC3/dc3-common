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

package io.github.pnoker.common.driver.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import io.github.pnoker.common.driver.entity.dto.DeviceDTO;
import io.github.pnoker.common.driver.metadata.DeviceMetadata;
import io.github.pnoker.common.driver.service.DriverReadService;
import io.github.pnoker.common.enums.EnableFlagEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 读任务
 * <p>
 * 系统内置定时任务
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class DriverReadScheduleJob extends QuartzJobBean {

    @Resource
    private DeviceMetadata deviceMetadata;
    @Resource
    private DriverReadService driverReadService;

    @Override
    protected void executeInternal(@NotNull JobExecutionContext jobExecutionContext) {
        List<DeviceDTO> entityDTOList = deviceMetadata.getAllDevice();
        if (CollUtil.isEmpty(entityDTOList)) {
            return;
        }

        for (DeviceDTO entityDTO : entityDTOList) {
            if (EnableFlagEnum.ENABLE.equals(entityDTO.getEnableFlag())
                    && CollUtil.isNotEmpty(entityDTO.getProfileIds())
                    && CollUtil.isNotEmpty(entityDTO.getPointIds())
                    && MapUtil.isNotEmpty(entityDTO.getDriverAttributeConfigMap())
                    && MapUtil.isNotEmpty(entityDTO.getPointAttributeConfigMap())
            ) {
                Set<Long> pointIds = entityDTO.getPointIds();
                for (Long pointId : pointIds) {
                    driverReadService.read(entityDTO.getId(), pointId);
                }
            }
        }
    }
}