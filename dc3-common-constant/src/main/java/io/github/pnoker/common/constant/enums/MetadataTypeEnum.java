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
 * 通用元数据类型枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum MetadataTypeEnum {
    /**
     * 元数据
     */
    METADATA((byte) 0, "metadata", "元数据"),

    /**
     * 模板类型元数据
     */
    PROFILE((byte) 1, "profile", "模板类型元数据"),

    /**
     * 位号类型元数据
     */
    POINT((byte) 2, "point", "位号类型元数据"),

    /**
     * 设备类型元数据
     */
    DEVICE((byte) 3, "device", "设备类型元数据"),

    /**
     * 驱动属性配置类型元数据
     */
    DRIVER_ATTRIBUTE_CONFIG((byte) 4, "driver_attribute_config", "驱动属性配置类型元数据"),

    /**
     * 位号属性配置类型元数据
     */
    POINT_ATTRIBUTE_CONFIG((byte) 5, "point_attribute_config", "位号属性配置类型元数据"),
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
     * @return {@link MetadataTypeEnum}
     */
    public static MetadataTypeEnum ofIndex(Byte index) {
        Optional<MetadataTypeEnum> any = Arrays.stream(MetadataTypeEnum.values()).filter(type -> type.getIndex().equals(index)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举编码获取枚举
     *
     * @param code 编码
     * @return {@link MetadataTypeEnum}
     */
    public static MetadataTypeEnum ofCode(String code) {
        Optional<MetadataTypeEnum> any = Arrays.stream(MetadataTypeEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举名称获取枚举
     *
     * @param name 枚举名称
     * @return {@link MetadataTypeEnum}
     */
    public static MetadataTypeEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
