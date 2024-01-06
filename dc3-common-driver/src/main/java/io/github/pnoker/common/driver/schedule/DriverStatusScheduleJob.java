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

import io.github.pnoker.common.DriverContext;
import io.github.pnoker.common.constant.enums.DriverEventTypeEnum;
import io.github.pnoker.common.entity.dto.DriverEventDTO;
import io.github.pnoker.common.driver.biz.DriverSenderService;
import io.github.pnoker.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自定义调度任务
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class DriverStatusScheduleJob extends QuartzJobBean {

    @Resource
    private DriverContext driverContext;
    @Resource
    private DriverSenderService driverSenderService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        DriverEventDTO.DriverStatus driverStatus = new DriverEventDTO.DriverStatus(driverContext.getDriverMetadataDTO().getDriverId(), driverContext.getDriverStatus());
        DriverEventDTO driverEventDTO = new DriverEventDTO(DriverEventTypeEnum.HEARTBEAT, JsonUtil.toJsonString(driverStatus));
        log.debug("上报驱动事件: {}, 事件内容: {}", driverEventDTO.getType().getCode(), JsonUtil.toJsonString(driverEventDTO));
        driverSenderService.driverEventSender(driverEventDTO);
    }
}