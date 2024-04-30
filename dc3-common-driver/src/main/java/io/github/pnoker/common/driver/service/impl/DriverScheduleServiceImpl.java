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
import io.github.pnoker.common.constant.driver.ScheduleConstant;
import io.github.pnoker.common.driver.job.DriverCustomScheduleJob;
import io.github.pnoker.common.driver.job.DriverReadScheduleJob;
import io.github.pnoker.common.driver.job.DriverStatusScheduleJob;
import io.github.pnoker.common.driver.service.DriverScheduleService;
import io.github.pnoker.common.entity.property.DriverProperty;
import io.github.pnoker.common.entity.property.ScheduleProperty;
import io.github.pnoker.common.quartz.QuartzService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverScheduleServiceImpl implements DriverScheduleService {

    @Resource
    private DriverProperty driverProperty;

    @Resource
    private QuartzService quartzService;

    @Override
    public void initial() {
        ScheduleProperty property = driverProperty.getSchedule();
        if (ObjectUtil.isNull(property)) {
            return;
        }

        try {
            if (Boolean.TRUE.equals(property.getRead().getEnable())) {
                quartzService.createJobWithCorn(ScheduleConstant.DRIVER_SCHEDULE_GROUP, ScheduleConstant.DRIVER_READ_SCHEDULE_JOB, property.getRead().getCorn(), DriverReadScheduleJob.class);
            }
            if (Boolean.TRUE.equals(property.getCustom().getEnable())) {
                quartzService.createJobWithCorn(ScheduleConstant.DRIVER_SCHEDULE_GROUP, ScheduleConstant.DRIVER_CUSTOM_SCHEDULE_JOB, property.getCustom().getCorn(), DriverCustomScheduleJob.class);
            }
            quartzService.createJobWithCorn(ScheduleConstant.DRIVER_SCHEDULE_GROUP, ScheduleConstant.DRIVER_STATUS_SCHEDULE_JOB, ScheduleConstant.DRIVER_STATUS_SCHEDULE_CORN, DriverStatusScheduleJob.class);


            quartzService.startScheduler();
        } catch (SchedulerException e) {
            log.error("Driver schedule initial error: {}", e.getMessage(), e);
        }
    }

}
