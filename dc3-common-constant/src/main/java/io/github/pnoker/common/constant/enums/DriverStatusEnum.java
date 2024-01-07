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
 * 驱动状态枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum DriverStatusEnum {
    ONLINE((byte) 0x00, "ONLINE", "在线"),
    OFFLINE((byte) 0x01, "OFFLINE", "离线"),
    MAINTAIN((byte) 0x02, "MAINTAIN", "维护"),
    FAULT((byte) 0x03, "FAULT", "故障"),
    ;

    /**
     * 索引
     */
    @EnumValue
    private final Byte index;

    /**
     * 状态编码
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
     * @return {@link DriverStatusEnum}
     */
    public static DriverStatusEnum ofIndex(Byte index) {
        Optional<DriverStatusEnum> any = Arrays.stream(DriverStatusEnum.values()).filter(type -> type.getIndex().equals(index)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举编码获取枚举
     *
     * @param code 编码
     * @return {@link DriverStatusEnum}
     */
    public static DriverStatusEnum ofCode(String code) {
        Optional<DriverStatusEnum> any = Arrays.stream(DriverStatusEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举名称获取枚举
     *
     * @param name 枚举名称
     * @return {@link DriverStatusEnum}
     */
    public static DriverStatusEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
