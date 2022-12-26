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

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.pnoker.common.bean.entity.BaseModel;
import io.github.pnoker.common.enums.EnableTypeEnum;
import io.github.pnoker.common.valid.Insert;
import io.github.pnoker.common.valid.Update;
import lombok.*;

import javax.validation.constraints.*;

/**
 * 驱动表
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
public class Driver extends BaseModel {

    @NotBlank(message = "Driver name can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_#@/.|]{1,31}$",
            message = "Invalid driver name,contains invalid characters or length is not in the range of 2~32",
            groups = {Insert.class, Update.class})
    private String driverName;

    @NotBlank(message = "Service name can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9-_]{1,31}$",
            message = "Invalid service name,contains invalid characters or length is not in the range of 2~32",
            groups = {Insert.class, Update.class})
    private String serviceName;

    // TODO:请使用枚举
    private Integer typeFlag;

    @NotBlank(message = "Service host can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$",
            message = "Invalid service host , /^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$/",
            groups = {Insert.class, Update.class})
    private String serverHost;

    @Min(value = 8600, message = "Invalid server port,port range is 8600-8799",
            groups = {Insert.class, Update.class})
    @Max(value = 8799, message = "Invalid server port,port range is 8600-8799",
            groups = {Insert.class, Update.class})
    private Integer serverPort;

    /**
     * 使能标识
     */
    private EnableTypeEnum enableFlag;

    /**
     * 租户ID
     */
    @NotBlank(message = "Tenant id can't be empty",
            groups = {Insert.class, Update.class})
    private String tenantId;

    // TODO:请使用枚举,提取到BO中
    @TableField(exist = false)
    private String status;
}
