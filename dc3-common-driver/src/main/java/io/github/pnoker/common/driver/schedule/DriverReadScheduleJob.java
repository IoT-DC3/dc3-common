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

package io.github.pnoker.common.driver.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.DriverContext;
import io.github.pnoker.common.driver.service.DriverCommandService;
import io.github.pnoker.common.entity.dto.AttributeConfigDTO;
import io.github.pnoker.common.entity.dto.DeviceDTO;
import io.github.pnoker.common.entity.dto.PointDTO;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Read Schedule Job
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class DriverReadScheduleJob extends QuartzJobBean {

    @Resource
    private DriverContext driverContext;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private DriverCommandService driverCommandService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<Long, DeviceDTO> deviceMap = driverContext.getDriverMetadataDTO().getDeviceMap();
        if (ObjectUtil.isNull(deviceMap)) {
            return;
        }

        for (DeviceDTO device : deviceMap.values()) {
            Set<Long> profileIds = device.getProfileIds();
            Map<Long, Map<String, AttributeConfigDTO>> pointInfoMap = driverContext.getDriverMetadataDTO().getPointInfoMap().get(device.getId());
            if (CollUtil.isEmpty(profileIds) || ObjectUtil.isNull(pointInfoMap)) {
                continue;
            }

            for (Long profileId : profileIds) {
                Map<Long, PointDTO> pointMap = driverContext.getDriverMetadataDTO().getProfilePointMap().get(profileId);
                if (ObjectUtil.isNull(pointMap)) {
                    continue;
                }

                for (Long pointId : pointMap.keySet()) {
                    Map<String, AttributeConfigDTO> map = pointInfoMap.get(pointId);
                    if (ObjectUtil.isNull(map)) {
                        continue;
                    }

                    threadPoolExecutor.execute(() -> driverCommandService.read(device.getId(), pointId));
                }
            }
        }
    }
}