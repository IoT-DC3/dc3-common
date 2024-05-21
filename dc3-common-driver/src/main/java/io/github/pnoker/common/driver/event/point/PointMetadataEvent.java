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

package io.github.pnoker.common.driver.event.point;

import io.github.pnoker.common.driver.entity.bo.PointBO;
import io.github.pnoker.common.enums.MetadataOperateTypeEnum;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 位号元数据事件
 *
 * @author zhangzi
 * @since 2022.1.0
 */
@Getter
public class PointMetadataEvent extends ApplicationEvent {

    private final Long id;
    private final MetadataOperateTypeEnum operateType;
    private final PointBO point;

    /**
     * 构造函数
     *
     * @param source      Object
     * @param id          位号ID
     * @param operateType 元数据操作类型
     * @param point       位号
     */
    public PointMetadataEvent(Object source, Long id, MetadataOperateTypeEnum operateType, PointBO point) {
        super(source);
        this.id = id;
        this.operateType = operateType;
        this.point = point;
    }
}
