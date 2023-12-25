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

package io.github.pnoker.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.pnoker.common.constant.enums.EnableFlagEnum;
import io.github.pnoker.common.constant.enums.GroupTypeFlagEnum;
import io.github.pnoker.common.entity.base.BaseVO;
import io.github.pnoker.common.valid.Add;
import io.github.pnoker.common.valid.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Group VO
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Schema(title = "Group", description = "分组")
public class GroupVO extends BaseVO {

    /**
     * 分组名称
     */
    @Schema(description = "分组名称")
    @NotBlank(message = "分组名称不能为空",
            groups = {Add.class})
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_#@/.|]{1,31}$",
            message = "分组名称格式无效",
            groups = {Add.class, Update.class})
    private String groupName;

    /**
     * 父分组ID
     */
    @Schema(description = "分组父级ID")
    private String parentGroupId;

    /**
     * 分组排序位置
     */
    @Schema(description = "分组排序")
    private Integer position;

    /**
     * 分组标识
     */
    @Schema(description = "分组标识")
    private GroupTypeFlagEnum groupTypeFlag;

    /**
     * 使能标识
     */
    @Schema(description = "使能标识")
    private EnableFlagEnum enableFlag;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private Long tenantId;
}
