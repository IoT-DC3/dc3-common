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

package io.github.pnoker.common.entity.bo;

import io.github.pnoker.common.entity.base.BaseBO;
import io.github.pnoker.common.enums.EnableFlagEnum;
import io.github.pnoker.common.enums.GroupTypeFlagEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Group BO
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupBO extends BaseBO {

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 父分组ID
     */
    private String parentGroupId;

    /**
     * 分组排序位置
     */
    private Integer position;

    /**
     * 分组标识
     */
    private GroupTypeFlagEnum groupTypeFlag;

    /**
     * 使能标识
     */
    private EnableFlagEnum enableFlag;

    /**
     * 租户ID
     */
    private Long tenantId;
}
