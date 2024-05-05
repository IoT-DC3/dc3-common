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
import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.driver.context.DriverContext;
import io.github.pnoker.common.driver.metadata.DeviceMetadata;
import io.github.pnoker.common.driver.metadata.PointMetadata;
import io.github.pnoker.common.driver.service.DriverCommandService;
import io.github.pnoker.common.entity.dto.DeviceDTO;
import io.github.pnoker.common.entity.dto.PointDTO;
import io.github.pnoker.common.enums.EnableFlagEnum;
import io.github.pnoker.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Read Schedule Job
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class DriverReadScheduleJob extends QuartzJobBean {

    private final DriverContext driverContext;
    private final DeviceMetadata deviceMetadata;
    private final PointMetadata pointMetadata;
    private final DriverCommandService driverCommandService;

    public DriverReadScheduleJob(DriverContext driverContext, DeviceMetadata deviceMetadata, PointMetadata pointMetadata, DriverCommandService driverCommandService) {
        this.driverContext = driverContext;
        this.deviceMetadata = deviceMetadata;
        this.pointMetadata = pointMetadata;
        this.driverCommandService = driverCommandService;
    }

    @Override
    protected void executeInternal(@NotNull JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<CompletableFuture<DeviceDTO>> deviceMetadataAllCache = deviceMetadata.getAllCache();
        for (CompletableFuture<DeviceDTO> future : deviceMetadataAllCache) {
            try {
                DeviceDTO deviceDTO = future.get();
                if (EnableFlagEnum.ENABLE.equals(deviceDTO.getEnableFlag())
                        && CollUtil.isNotEmpty(deviceDTO.getProfileIds())
                        && MapUtil.isNotEmpty(deviceDTO.getDriverAttributeConfigMap())
                        && MapUtil.isNotEmpty(deviceDTO.getPointAttributeConfigMap())) {
                    Set<Long> pointIds = deviceDTO.getPointIds();
                    for (Long pointId : pointIds) {
                        driverCommandService.read(deviceDTO.getId(), pointId);
                    }
                }


                log.info("1----{}", JsonUtil.toJsonString(deviceDTO));
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        List<CompletableFuture<PointDTO>> pointMetadataAllCache = pointMetadata.getAllCache();
        for (CompletableFuture<PointDTO> future : pointMetadataAllCache) {
            try {
                PointDTO pointDTO = future.get();
                log.info("2----{}", JsonUtil.toJsonString(pointDTO));
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }


        Map<Long, DeviceDTO> deviceMap = driverContext.getDriverMetadata().getDeviceMap();
        if (ObjectUtil.isNull(deviceMap)) {
            return;
        }

        List<DeviceDTO> deviceDTOS = deviceMap.values().stream()
                .filter(deviceDTO -> EnableFlagEnum.ENABLE.equals(deviceDTO.getEnableFlag())
                        && CollUtil.isNotEmpty(deviceDTO.getProfileIds())
                        && MapUtil.isNotEmpty(driverContext.getDriverMetadata().getDriverConfigMap().get(deviceDTO.getId()))
                        && MapUtil.isNotEmpty(driverContext.getDriverMetadata().getPointConfigMap().get(deviceDTO.getId()))
                )
                .toList();

        Map<Long, Set<Long>> devicePointMap = deviceDTOS.stream().collect(
                Collectors.toMap(
                        DeviceDTO::getId,
                        deviceDTO -> deviceDTO.getProfileIds().stream()
                                .map(profileId -> driverContext.getDriverMetadata().getProfilePointMap().get(profileId))
                                .filter(MapUtil::isNotEmpty)
                                .flatMap(pointMap -> pointMap.keySet().stream())
                                .collect(Collectors.toSet())
                )
        );

        for (Map.Entry<Long, Set<Long>> entry : devicePointMap.entrySet()) {
            for (Long pointId : entry.getValue()) {
                driverCommandService.read(entry.getKey(), pointId);
            }
        }
    }
}