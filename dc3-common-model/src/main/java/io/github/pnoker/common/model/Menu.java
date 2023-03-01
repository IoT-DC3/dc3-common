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
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.pnoker.common.enums.EnableFlagEnum;
import io.github.pnoker.common.enums.MenuTypeFlagEnum;
import io.github.pnoker.common.valid.Auth;
import io.github.pnoker.common.valid.Insert;
import io.github.pnoker.common.valid.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 菜单表
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
public class Menu extends Base {

    /**
     * 菜单父级ID
     */
    @NotBlank(message = "Menu parent id can't be empty",
            groups = {Insert.class, Update.class})
    private String parentMenuId;

    /**
     * 菜单类型标识
     */
    private MenuTypeFlagEnum menuTypeFlag;

    /**
     * 菜单名称
     */
    @NotBlank(message = "Menu name can't be empty",
            groups = {Insert.class, Auth.class})
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9-_#@/.|]{1,31}$",
            message = "Invalid menu name",
            groups = {Insert.class, Update.class})
    private String menuName;

    /**
     * 菜单编号，一般为URL的MD5编码
     */
    private String menuCode;

    /**
     * 菜单层级
     */
    private Integer menuLevel;

    /**
     * 菜单顺序
     */
    private Integer menuIndex;

    /**
     * 菜单拓展信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private MenuExt menuExt;

    /**
     * 使能标识
     */
    private EnableFlagEnum enableFlag;

    /**
     * 租户ID
     */
    @NotBlank(message = "Tenant id can't be empty",
            groups = {Insert.class, Update.class})
    private String tenantId;
}
