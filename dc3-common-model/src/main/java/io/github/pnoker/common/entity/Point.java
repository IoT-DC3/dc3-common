/*
 * Copyright 2016-present Pnoker All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.pnoker.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.pnoker.common.bean.entity.BaseModel;
import io.github.pnoker.common.enums.*;
import io.github.pnoker.common.valid.Insert;
import io.github.pnoker.common.valid.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * 设备变量表
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Point extends BaseModel {

    /**
     * 位号名称
     */
    @NotBlank(message = "Point name can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_#@/.|]{1,31}$",
            message = "Invalid point name",
            groups = {Insert.class, Update.class})
    private String pointName;

    /**
     * 位号编号
     */
    @NotBlank(message = "Point code can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9-_#@/.|]{1,31}$",
            message = "Invalid point code",
            groups = {Insert.class, Update.class})
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
    private BigDecimal base;

    /**
     * 比例系数
     */
    private BigDecimal multiple;

    /**
     * 累计标识
     */
    private AccrueFlagEnum accrueFlag;

    /**
     * 数据精度
     */
    private Byte valueDecimal;

    /**
     * 单位
     */
    private UnitEnum unit;

    /**
     * 模板ID
     */
    @NotBlank(message = "Profile id can't be empty",
            groups = {Insert.class, Update.class})
    private String profileId;

    /**
     * 分组ID
     */
    // TODO 后期再实现分组，先放着占个坑 @NotNull(message = "group id can't be empty", groups = {Insert.class, Update.class})
    private String groupId;

    /**
     * 使能标识
     */
    private EnableFlagEnum enableFlag;

    /**
     * 租户ID
     */
    @NotBlank(message = "Tenant id can't be empty",
            groups = {Insert.class, Update.class})
    private String tenantId;

    /**
     * 设置默认值
     */
    public void setDefault() {
        this.pointTypeFlag = PointTypeFlagEnum.STRING;
        this.rwFlag = RwFlagEnum.R;
        this.base = BigDecimal.valueOf(0);
        this.multiple = BigDecimal.valueOf(1);
        this.accrueFlag = AccrueFlagEnum.NONE;
        this.valueDecimal = 6;
        this.unit = UnitEnum.NONE;
    }

}
