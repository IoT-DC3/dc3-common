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

package io.github.pnoker.common.entity.dto;

import io.github.pnoker.common.base.BaseDTO;
import io.github.pnoker.common.constant.enums.AttributeTypeFlagEnum;
import io.github.pnoker.common.constant.enums.EnableFlagEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * PointAttribute BO
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PointAttributeDTO extends BaseDTO {

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 属性类型标识
     */
    private AttributeTypeFlagEnum attributeTypeFlag;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 驱动ID
     */
    private Long driverId;

    /**
     * 使能标识
     */
    private EnableFlagEnum enableFlag;

    /**
     * 租户ID
     */
    private Long tenantId;
}
