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
 * 通用单位类型枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum UnitTypeEnum {
    /**
     * 无单位
     */
    NONE("", "无单位"),

    /**
     * 摄氏度
     */
    CENTIGRADE("℃", "摄氏度"),

    /**
     * 克
     */
    G("g", "克"),

    /**
     * 千克
     */
    KG("Kg", "千克"),
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
     * @return UnitTypeEnum
     */
    public static UnitTypeEnum of(String code) {
        Optional<UnitTypeEnum> any = Arrays.stream(UnitTypeEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }
}
