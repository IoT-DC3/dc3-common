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

package io.github.pnoker.common.entity.common;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础查询类，其中包括分页以及排序
 *
 * @author pnoker
 * @since 2022.1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页")
public class Pages implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "当前分页，默认1")
    private long current = 1;

    @Schema(description = "分页条数")
    private long size = 20;

    @Schema(description = "开始时间戳，毫秒")
    private long startTime;

    @Schema(description = "结束时间戳，毫秒")
    private long endTime;

    @Schema(title = "排序", description = "column:需要进行排序的字段属性名, asc:是否正序排列，默认 true")
    private List<OrderItem> orders = new ArrayList<>(2);
}
