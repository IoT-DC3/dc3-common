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

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用属性类型标识枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum AttributeTypeFlagEnum {
    /**
     * 字符串
     */
    STRING(0, "string", "字符串"),

    /**
     * 字节
     */
    BYTE(1, "byte", "字节"),

    /**
     * 短整数
     */
    SHORT(2, "short", "短整数"),

    /**
     * 整数
     */
    INT(3, "int", "整数"),

    /**
     * 长整数
     */
    LONG(4, "long", "长整数"),

    /**
     * 浮点数
     */
    FLOAT(5, "float", "浮点数"),

    /**
     * 双精度浮点数
     */
    DOUBLE(6, "double", "双精度浮点数"),

    /**
     * 布尔量
     */
    BOOLEAN(7, "boolean", "布尔量"),
    ;

    /**
     * 索引
     */
    @EnumValue
    private final Integer index;

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
     * @return AttributeTypeFlagEnum
     */
    public static AttributeTypeFlagEnum of(String code) {
        Optional<AttributeTypeFlagEnum> any = Arrays.stream(AttributeTypeFlagEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }
}
