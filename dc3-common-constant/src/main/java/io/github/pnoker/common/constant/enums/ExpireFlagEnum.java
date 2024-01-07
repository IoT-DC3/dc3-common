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
 * 通用失效类型枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum ExpireFlagEnum {
    /**
     * 永久
     */
    PERMANENT((byte) 0x00, "permanent", "永久"),

    /**
     * 一天
     */
    ONE_DAY((byte) 0x01, "one_day", "一天"),

    /**
     * 一周
     */
    ONE_WEEK((byte) 0x02, "one_week", "一周"),

    /**
     * 一月
     */
    ONE_MONTH((byte) 0x03, "one_month", "一月"),

    /**
     * 三月
     */
    THREE_MONTHS((byte) 0x04, "three_months", "三月"),

    /**
     * 半年
     */
    HALF_YEAR((byte) 0x05, "half_year", "半年"),

    /**
     * 一年
     */
    ONE_YEAR((byte) 0x06, "one_year", "一年"),

    /**
     * 自定义
     */
    CUSTOM((byte) 0x07, "custom", "自定义"),
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
     * 根据枚举索引获取枚举
     *
     * @param index 索引
     * @return {@link ExpireFlagEnum}
     */
    public static ExpireFlagEnum ofIndex(Byte index) {
        Optional<ExpireFlagEnum> any = Arrays.stream(ExpireFlagEnum.values()).filter(type -> type.getIndex().equals(index)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举编码获取枚举
     *
     * @param code 编码
     * @return {@link ExpireFlagEnum}
     */
    public static ExpireFlagEnum ofCode(String code) {
        Optional<ExpireFlagEnum> any = Arrays.stream(ExpireFlagEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举名称获取枚举
     *
     * @param name 枚举名称
     * @return {@link ExpireFlagEnum}
     */
    public static ExpireFlagEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
