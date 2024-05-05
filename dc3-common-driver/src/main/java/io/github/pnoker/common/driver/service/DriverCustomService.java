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

package io.github.pnoker.common.driver.service;

import io.github.pnoker.common.entity.bo.AttributeBO;
import io.github.pnoker.common.entity.dto.DeviceDTO;
import io.github.pnoker.common.entity.dto.PointDTO;

import java.util.Map;

/**
 * 自定义驱动接口, 开发的自定义驱动至少需要实现 read 和 write 接口, 可以参考以提供的驱动模块写法
 *
 * @author pnoker
 * @since 2022.1.0
 */
public interface DriverCustomService {
    /**
     * 初始化接口, 会在驱动启动时执行
     */
    void initial();

    /**
     * 自定义调度接口, 配置文件 driver.schedule.custom 进行配置
     */
    void schedule();

    /**
     * 读操作, 请灵活运行, 有些类型设备不一定能直接读取数据
     *
     * @param driverConfig Driver Attribute Config
     * @param pointConfig  Point Attribute Config
     * @param device       Device
     * @param point        Point
     * @return R of String Value
     */
    String read(Map<String, AttributeBO> driverConfig, Map<String, AttributeBO> pointConfig, DeviceDTO device, PointDTO point);

    /**
     * 写操作, 请灵活运行, 有些类型设备不一定能直接写入数据
     *
     * @param driverConfig Driver Attribute Config
     * @param pointConfig  Point Attribute Config
     * @param device       Device
     * @param value        Value Attribute Config
     * @return Boolean 是否写入
     */
    Boolean write(Map<String, AttributeBO> driverConfig, Map<String, AttributeBO> pointConfig, DeviceDTO device, AttributeBO value);

}
