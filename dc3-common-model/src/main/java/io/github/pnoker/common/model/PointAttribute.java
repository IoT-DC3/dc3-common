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

import io.github.pnoker.common.bean.model.BaseModel;
import io.github.pnoker.common.valid.Insert;
import io.github.pnoker.common.valid.Update;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 模板配置信息表
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PointAttribute extends BaseModel {

    @NotBlank(message = "Display name can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_]{0,31}$",
            message = "Invalid display name,contains invalid characters or length is not in the range of 1~32",
            groups = {Insert.class, Update.class})
    private String displayName;

    @NotBlank(message = "Name can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9-_]{1,31}$",
            message = "Invalid name,contains invalid characters or length is not in the range of 2~32",
            groups = {Insert.class, Update.class})
    private String name;

    /**
     * string/int/double/float/long/boolean
     */
    private String type;
    private String value;

    @NotNull(message = "Driver id can't be empty", groups = {Insert.class, Update.class})
    private String driverId;
}
