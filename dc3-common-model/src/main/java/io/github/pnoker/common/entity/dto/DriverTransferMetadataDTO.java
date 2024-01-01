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

import io.github.pnoker.common.constant.enums.MetadataCommandTypeEnum;
import io.github.pnoker.common.constant.enums.MetadataTypeEnum;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 元数据
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverTransferMetadataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 元数据类型
     */
    private MetadataTypeEnum type;

    /**
     * 元数据操作类型
     */
    private MetadataCommandTypeEnum metadataCommandType;

    /**
     * 元数据内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    public DriverTransferMetadataDTO(MetadataTypeEnum type, MetadataCommandTypeEnum metadataCommandType, String content) {
        this.type = type;
        this.metadataCommandType = metadataCommandType;
        this.content = content;
        this.createTime = LocalDateTime.now();
    }
}
