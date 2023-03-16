/*
 * Copyright 2016-present the original author or authors.
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

package io.github.pnoker.common.constant.driver;

import io.github.pnoker.common.constant.common.ExceptionConstant;

/**
 * 任务调度 相关常量
 *
 * @author pnoker
 * @since 2022.1.0
 */
public class ScheduleConstant {

    private ScheduleConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 驱动任务调度分组
     */
    public static final String DRIVER_SCHEDULE_GROUP = "DriverScheduleGroup";

    /**
     * 读任务
     */
    public static final String READ_SCHEDULE_JOB = "ReadScheduleJob";

    /**
     * 自定义任务
     */
    public static final String CUSTOM_SCHEDULE_JOB = "CustomScheduleJob";

    /**
     * 状态任务
     */
    public static final String STATUS_SCHEDULE_JOB = "StatusScheduleJob";

    /**
     * 驱动状态任务 Corn
     */
    public static final String DRIVER_STATUS_CORN = "0/15 * * * * ?";
}
