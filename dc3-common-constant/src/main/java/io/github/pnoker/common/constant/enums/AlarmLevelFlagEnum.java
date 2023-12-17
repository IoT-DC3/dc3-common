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
 * 通用报警等级类型枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum AlarmLevelFlagEnum {
    /**
     * P0
     */
    P0((byte) 0x00, "p0", "P0"),

    /**
     * P1
     */
    P1((byte) 0x01, "p1", "P1"),

    /**
     * P2
     */
    P2((byte) 0x02, "p2", "P2"),

    /**
     * P3
     */
    P3((byte) 0x02, "p3", "P3"),
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
     * @return {@link AlarmLevelFlagEnum}
     */
    public static AlarmLevelFlagEnum ofCode(String code) {
        Optional<AlarmLevelFlagEnum> any = Arrays.stream(AlarmLevelFlagEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举名称获取枚举
     *
     * @param name 枚举名称
     * @return {@link AlarmLevelFlagEnum}
     */
    public static AlarmLevelFlagEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}