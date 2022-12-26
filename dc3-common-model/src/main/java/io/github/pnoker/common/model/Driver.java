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

package io.github.pnoker.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.pnoker.common.bean.model.BaseModel;
import io.github.pnoker.common.valid.Insert;
import io.github.pnoker.common.valid.Update;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
public class Driver extends BaseModel {

    @NotBlank(message = "Name can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_#@/.|]{1,31}$",
            message = "Invalid name,contains invalid characters or length is not in the range of 2~32",
            groups = {Insert.class, Update.class})
    private String name;

    @NotBlank(message = "Service name can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9-_]{1,31}$",
            message = "Invalid service name,contains invalid characters or length is not in the range of 2~32", groups = {Insert.class, Update.class})
    private String serviceName;

    @NotBlank(message = "Host can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$",
            message = "Invalid ip , /^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$/",
            groups = {Insert.class, Update.class})
    private String host;

    private String type;

    @Min(value = 8600, message = "Invalid port,port range is 8600-8799",
            groups = {Insert.class, Update.class})
    @Max(value = 8799, message = "Invalid port,port range is 8600-8799",
            groups = {Insert.class, Update.class})
    private Integer port;

    private Boolean enable;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String tenantId;

    @TableField(exist = false)
    private String status;

    public Driver(String name, String serviceName, String host, Integer port, String type) {
        super();
        this.name = name;
        this.serviceName = serviceName;
        this.host = host;
        this.port = port;
        this.type = type;
    }
}
