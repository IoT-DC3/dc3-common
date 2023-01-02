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

package io.github.pnoker.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用位号类型标识枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum PointTypeFlagEnum {
    /**
     * 字符串
     */
    STRING("string", "字符串"),

    /**
     * 字节
     */
    BYTE("byte", "字节"),

    /**
     * 短整数
     */
    SHORT("short", "短整数"),

    /**
     * 整数
     */
    INT("int", "整数"),

    /**
     * 长整数
     */
    LONG("long", "长整数"),

    /**
     * 浮点数
     */
    FLOAT("float", "浮点数"),

    /**
     * 双精度浮点数
     */
    DOUBLE("double", "双精度浮点数"),

    /**
     * 布尔量
     */
    BOOLEAN("boolean", "布尔量"),
    ;

    /**
     * 编码
     */
    private final String code;

    /**
     * 名称
     */
    private final String name;

    /**
     * 根据 Code 获取枚举
     *
     * @param code Code
     * @return PointTypeFlagEnum
     */
    public static PointTypeFlagEnum of(String code) {
        Optional<PointTypeFlagEnum> any = Arrays.stream(PointTypeFlagEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }
}
