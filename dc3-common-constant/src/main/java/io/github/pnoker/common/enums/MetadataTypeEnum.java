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

package io.github.pnoker.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用元数据类型枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum MetadataTypeEnum {
    /**
     * 驱动类型元数据
     */
    DRIVER((byte) 0x00, "driver", "驱动类型元数据"),

    /**
     * 模板类型元数据
     */
    PROFILE((byte) 0x01, "profile", "模板类型元数据"),

    /**
     * 位号类型元数据
     */
    POINT((byte) 0x02, "point", "位号类型元数据"),

    /**
     * 设备类型元数据
     */
    DEVICE((byte) 0x03, "device", "设备类型元数据"),

    /**
     * 驱动配置类型元数据
     */
    DRIVER_INFO((byte) 0x04, "driver_info", "驱动配置类型元数据"),

    /**
     * 位号配置类型元数据
     */
    POINT_INFO((byte) 0x05, "point_info", "位号配置类型元数据"),
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
     * 名称
     */
    private final String name;

    /**
     * 根据 Code 获取枚举
     *
     * @param code Code
     * @return MetadataTypeEnum
     */
    public static MetadataTypeEnum of(String code) {
        Optional<MetadataTypeEnum> any = Arrays.stream(MetadataTypeEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }
}
