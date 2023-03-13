/*
 * Copyright 2016-present the original author or authors.
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

package io.github.pnoker.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.pnoker.common.entity.base.Base;
import io.github.pnoker.common.enums.EnableFlagEnum;
import io.github.pnoker.common.enums.ResourceTypeFlagEnum;
import io.github.pnoker.common.valid.Auth;
import io.github.pnoker.common.valid.Insert;
import io.github.pnoker.common.valid.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 权限资源表
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
public class Resource extends Base {

    /**
     * 权限资源父级ID
     */
    @NotBlank(message = "Resource parent id can't be empty",
            groups = {Insert.class, Update.class})
    private String parentResourceId;

    /**
     * 权限资源名称
     */
    @NotBlank(message = "Role name can't be empty",
            groups = {Insert.class, Auth.class})
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9-_#@/.|]{1,31}$",
            message = "Invalid role name",
            groups = {Insert.class, Update.class})
    private String resourceName;

    /**
     * 权限资源编号
     */
    private String resourceCode;

    /**
     * 权限资源类型标识
     */
    private ResourceTypeFlagEnum resourceTypeFlag;

    /**
     * 权限资源范围标识，参考：ResourceScopeFlagEnum
     * <ul>
     *     <li>0x01：新增</li>
     *     <li>0x02：删除</li>
     *     <li>0x04：修改</li>
     *     <li>0x08：查询</li>
     * </ul>
     * 具有多个权限范围可以累加
     */
    private Byte resourceScopeFlag;

    /**
     * 权限资源实体ID
     */
    @NotBlank(message = "Entity id can't be empty",
            groups = {Insert.class, Update.class})
    private String entityId;

    /**
     * 使能标识
     */
    private EnableFlagEnum enableFlag;

    /**
     * 租户ID
     */
    private String tenantId;
}
