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

package io.github.pnoker.common.init;

import io.github.pnoker.common.driver.biz.DriverCustomService;
import io.github.pnoker.common.driver.biz.DriverScheduleService;
import io.github.pnoker.common.driver.biz.DriverSyncService;
import io.github.pnoker.common.entity.property.DriverProperty;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Driver Initial
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Component
@ComponentScan(basePackages = {
        "io.github.pnoker.common.driver.*"
})
@EnableConfigurationProperties({DriverProperty.class})
public class DriverInitRunner implements ApplicationRunner {

    @Resource
    private DriverSyncService driverSyncService;
    @Resource
    private DriverCustomService driverCustomService;
    @Resource
    private DriverScheduleService driverScheduleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 驱动同步
        driverSyncService.up();

        // 执行驱动模块的自定义初始化函数
        driverCustomService.initial();

        // 初始化驱动任务，包括驱动状态、读和自定义任务
        driverScheduleService.initial();
    }
}
