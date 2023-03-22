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
 * 通用驱动事件枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum DriverEventEnum {
    /**
     * 注册事件
     */
    REGISTER((byte) 0x00, "register", "注册事件"),

    /**
     * 心跳事件
     */
    HEARTBEAT((byte) 0x01, "heartbeat", "心跳事件"),

    /**
     * 报警事件
     */
    ALARM((byte) 0x02, "alarm", "报警事件"),
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
     * @return DriverEventEnum
     */
    public static DriverEventEnum of(String code) {
        Optional<DriverEventEnum> any = Arrays.stream(DriverEventEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }
}
