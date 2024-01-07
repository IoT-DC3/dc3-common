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

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB 位号数据
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointValueDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 位号ID
     */
    private Long pointId;

    /**
     * 处理值
     */
    private String value;

    /**
     * 原始值
     */
    private String rawValue;

    private List<String> children;

    /**
     * 原始时间
     */
    private LocalDateTime originTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    public PointValueDTO(Long deviceId, Long pointId, String rawValue, String value) {
        this.deviceId = deviceId;
        this.pointId = pointId;
        this.rawValue = rawValue;
        this.value = value;
        this.originTime = LocalDateTime.now();
    }
}
