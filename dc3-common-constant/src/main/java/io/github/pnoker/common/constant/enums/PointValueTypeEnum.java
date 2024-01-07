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
 * 通用位号数据类型枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum PointValueTypeEnum {
    BYTE((byte) 0x00, "byte", "字节"),
    SHORT((byte) 0x01, "short", "短整数"),
    INT((byte) 0x02, "int", "整数"),
    LONG((byte) 0x03, "long", "长整数"),
    FLOAT((byte) 0x04, "float", "浮点数"),
    DOUBLE((byte) 0x05, "double", "双精度浮点数"),
    BOOLEAN((byte) 0x06, "boolean", "布尔量"),
    STRING((byte) 0x07, "string", "字符串"),
    HEX((byte) 0x08, "hex", "十六进制");

    /**
     * 索引
     */
    @EnumValue
    private final Byte index;

    /**
     * 位号数据类型编码
     */
    private final String code;

    /**
     * 备注
     */
    private final String remark;

    /**
     * 根据枚举索引获取枚举
     *
     * @param index 索引
     * @return {@link PointValueTypeEnum}
     */
    public static PointValueTypeEnum ofIndex(Byte index) {
        Optional<PointValueTypeEnum> any = Arrays.stream(PointValueTypeEnum.values()).filter(type -> type.getIndex().equals(index)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举编码获取枚举
     *
     * @param code 编码
     * @return {@link PointValueTypeEnum}
     */
    public static PointValueTypeEnum ofCode(String code) {
        Optional<PointValueTypeEnum> any = Arrays.stream(PointValueTypeEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举名称获取枚举
     *
     * @param name 枚举名称
     * @return {@link PointValueTypeEnum}
     */
    public static PointValueTypeEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
