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

package io.github.pnoker.common.entity.query;

import io.github.pnoker.common.entity.common.Pages;
import io.github.pnoker.common.enums.EnableFlagEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * PointValue Query
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "PointValue", description = "位号值-查询")
public class PointValueQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "分页")
    private Pages page;

    /**
     * 租户ID
     */
    @Schema(description = "使能标识", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long tenantId;

    // 查询字段

    /**
     * 设备ID
     */
    @Schema(description = "设备ID")
    private Long deviceId;

    /**
     * 设备名称
     */
    @Schema(description = "设备名称")
    private String deviceName;

    /**
     * 位号ID
     */
    @Schema(description = "位号ID")
    private Long pointId;

    /**
     * 位号名称
     */
    @Schema(description = "位号名称")
    private String pointName;

    /**
     * 使能标识
     */
    @Schema(description = "使能标识")
    private EnableFlagEnum enableFlag;

}
