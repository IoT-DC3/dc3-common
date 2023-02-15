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
 * 通用模板共享类型标识枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum ProfileShareFlagEnum {
    /**
     * 公有模板(租户)
     */
    TENANT(0, "tenant", "公有模板(租户)"),

    /**
     * 私有模板(驱动)
     */
    DRIVER(1, "driver", "私有模板(驱动)"),

    /**
     * 私有模板(用户)
     */
    USER(2, "user", "私有模板(用户)"),
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
     * @return ProfileShareFlagEnum
     */
    public static ProfileShareFlagEnum of(String code) {
        Optional<ProfileShareFlagEnum> any = Arrays.stream(ProfileShareFlagEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }
}
