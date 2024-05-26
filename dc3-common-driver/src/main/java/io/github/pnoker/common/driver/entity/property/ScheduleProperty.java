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

package io.github.pnoker.common.driver.entity.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 驱动配置文件 driver.schedule 字段内容
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@Setter
public class ScheduleProperty {

    /**
     * 读任务配置
     */
    private ScheduleConfig read;

    /**
     * 自定义任务配置
     */
    private ScheduleConfig custom;

    /**
     * 驱动调度任务配置
     *
     * @author pnoker
     * @since 2022.1.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleConfig {
        private Boolean enable = false;
        private String cron = "* */15 * * * ?";
    }
}
