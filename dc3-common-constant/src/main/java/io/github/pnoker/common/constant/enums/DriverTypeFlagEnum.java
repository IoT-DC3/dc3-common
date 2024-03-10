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

package io.github.pnoker.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用驱动类型标识枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum DriverTypeFlagEnum {
    /**
     * 协议驱动
     */
    DRIVER((byte) 0, "driver", "协议类型驱动"),

    /**
     * 网关驱动
     */
    GATEWAY((byte) 1, "gateway", "网关类型驱动"),

    /**
     * 串联驱动
     */
    CONNECT((byte) 2, "connect", "串联类型驱动"),
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
     * 内容
     */
    private final String remark;

    /**
     * 根据枚举索引获取枚举
     *
     * @param index 索引
     * @return {@link DriverTypeFlagEnum}
     */
    public static DriverTypeFlagEnum ofIndex(Byte index) {
        Optional<DriverTypeFlagEnum> any = Arrays.stream(DriverTypeFlagEnum.values()).filter(type -> type.getIndex().equals(index)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举编码获取枚举
     *
     * @param code 编码
     * @return {@link DriverTypeFlagEnum}
     */
    public static DriverTypeFlagEnum ofCode(String code) {
        Optional<DriverTypeFlagEnum> any = Arrays.stream(DriverTypeFlagEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举名称获取枚举
     *
     * @param name 枚举名称
     * @return {@link DriverTypeFlagEnum}
     */
    public static DriverTypeFlagEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
