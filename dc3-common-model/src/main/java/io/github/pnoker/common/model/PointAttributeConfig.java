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

package io.github.pnoker.common.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.pnoker.common.entity.base.Base;
import io.github.pnoker.common.enums.EnableFlagEnum;
import io.github.pnoker.common.valid.Insert;
import io.github.pnoker.common.valid.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 位号属性配置表
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
@TableName("dc3_point_attribute_config")
public class PointAttributeConfig extends Base {

    /**
     * 位号配置ID
     */
    @NotBlank(message = "Point attribute id can't be empty",
            groups = {Insert.class, Update.class})
    private String pointAttributeId;

    /**
     * 位号配置值
     */
    @NotNull(message = "Point config value can't be empty")
    private String configValue;

    /**
     * 设备ID
     */
    @NotBlank(message = "Device id can't be empty",
            groups = {Insert.class, Update.class})
    private String deviceId;

    /**
     * 位号ID
     */
    @NotBlank(message = "Point id can't be empty",
            groups = {Insert.class, Update.class})
    private String pointId;

    /**
     * 使能标识
     */
    private EnableFlagEnum enableFlag;

    /**
     * 租户ID
     */
    private String tenantId;
}
