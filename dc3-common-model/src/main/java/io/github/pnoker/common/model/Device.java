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

package io.github.pnoker.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.pnoker.common.entity.base.Base;
import io.github.pnoker.common.enums.EnableFlagEnum;
import io.github.pnoker.common.valid.Insert;
import io.github.pnoker.common.valid.Update;
import io.github.pnoker.common.valid.Upload;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

/**
 * 设备表
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
@TableName("dc3_device")
public class Device extends Base {

    /**
     * 设备名称
     */
    @NotBlank(message = "Device name can't be empty", groups = {Insert.class})
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_#@/.|]{1,31}$",
            message = "Invalid device name",
            groups = {Insert.class, Update.class})
    private String deviceName;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 驱动ID
     */
    @NotBlank(message = "Driver id can't be empty",
            groups = {Insert.class, Update.class, Upload.class})
    private Long driverId;

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

    @TableField(exist = false)
    @NotNull(message = "Profile id set can't be empty",
            groups = {Upload.class})
    private Set<Long> profileIds = new HashSet<>(8);
}
