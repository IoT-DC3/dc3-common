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

package io.github.pnoker.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用菜单类型枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum MenuTypeFlagEnum {

    /**
     * 标题菜单
     */
    TITLE((byte) 0x00, "title", "标题菜单"),

    /**
     * 通用菜单
     */
    COMMON((byte) 0x01, "common", "通用菜单"),
    ;

    /**
     * 索引
     */
    @EnumValue
    private final Byte index;

    /**
     * 编码
     */
    private final String code;

    /**
     * 备注
     */
    private final String remark;

    /**
     * 根据枚举编码获取枚举
     *
     * @param code 编码
     * @return {@link MenuTypeFlagEnum}
     */
    public static MenuTypeFlagEnum ofCode(String code) {
        Optional<MenuTypeFlagEnum> any = Arrays.stream(MenuTypeFlagEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举名称获取枚举
     *
     * @param name 枚举名称
     * @return {@link MenuTypeFlagEnum}
     */
    public static MenuTypeFlagEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
