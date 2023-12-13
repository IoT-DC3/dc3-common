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
import io.github.pnoker.common.constant.enums.EnableFlagEnum;
import io.github.pnoker.common.constant.enums.PointTypeFlagEnum;
import io.github.pnoker.common.constant.enums.RwFlagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Point BO
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PointDTO extends BaseDTO {

    /**
     * 位号名称
     */
    private String pointName;

    /**
     * 位号编号
     */
    private String pointCode;

    /**
     * 位号类型标识
     */
    private PointTypeFlagEnum pointTypeFlag;

    /**
     * 读写标识
     */
    private RwFlagEnum rwFlag;

    /**
     * 基础值
     */
    private BigDecimal baseValue;

    /**
     * 比例系数
     */
    private BigDecimal multiple;

    /**
     * 数据精度
     */
    private Byte valueDecimal;

    /**
     * 单位
     */
    private String unit;

    /**
     * 模板ID
     */
    private Long profileId;

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 使能标识
     */
    private EnableFlagEnum enableFlag;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 设置默认值
     */
    public void setDefault() {
        this.pointTypeFlag = PointTypeFlagEnum.STRING;
        this.rwFlag = RwFlagEnum.R;
        this.baseValue = BigDecimal.valueOf(0);
        this.multiple = BigDecimal.valueOf(1);
        this.valueDecimal = 6;
        this.unit = "";
    }

}
