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

import javax.validation.constraints.NotNull;

/**
 * 位号配置信息表
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
public class PointInfo extends BaseModel {

    @NotNull(message = "Point attribute id can't be empty", groups = {Insert.class, Update.class})
    private String pointAttributeId;

    @NotNull(message = "Point attribute value can't be empty", groups = {Insert.class, Update.class})
    private String value;

    @NotNull(message = "Device id can't be empty", groups = {Insert.class, Update.class})
    private String deviceId;

    @NotNull(message = "Point id can't be empty", groups = {Insert.class, Update.class})
    private String pointId;
}
