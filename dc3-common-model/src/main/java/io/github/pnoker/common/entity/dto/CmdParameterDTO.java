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

import io.github.pnoker.common.valid.Read;
import io.github.pnoker.common.valid.Write;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 指令参数
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@SuperBuilder
public class CmdParameterDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    @NotNull(message = "设备ID不能为空",
            groups = {Read.class, Write.class})
    private Long deviceId;

    /**
     * 位号ID
     */
    @NotNull(message = "位号ID不能为空",
            groups = {Read.class, Write.class})
    private Long pointId;

    /**
     * 写入值
     */
    @NotBlank(message = "Value can't be empty",
            groups = {Write.class})
    private String value;
}
