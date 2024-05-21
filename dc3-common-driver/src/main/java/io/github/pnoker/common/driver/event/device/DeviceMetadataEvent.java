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

package io.github.pnoker.common.driver.event.device;

import io.github.pnoker.common.driver.entity.bo.DeviceBO;
import io.github.pnoker.common.enums.MetadataOperateTypeEnum;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 设备元数据事件
 *
 * @author zhangzi
 * @since 2022.1.0
 */
@Getter
public class DeviceMetadataEvent extends ApplicationEvent {

    private final Long id;
    private final MetadataOperateTypeEnum operateType;
    private final DeviceBO device;

    /**
     * 构造函数
     *
     * @param source      Object
     * @param id          设备ID
     * @param operateType 元数据操作类型
     * @param device      设备
     */
    public DeviceMetadataEvent(Object source, Long id, MetadataOperateTypeEnum operateType, DeviceBO device) {
        super(source);
        this.id = id;
        this.operateType = operateType;
        this.device = device;
    }
}
