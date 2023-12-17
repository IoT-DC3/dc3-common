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

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用返回结果枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    OK("R200", "成功"),
    TOKEN_INVALID("R20301", "令牌无效"),
    IP_INVALID("R20302", "IP无效"),
    FAILURE("R500", "服务异常"),
    NO_RESOURCE("R404", "资源不存在"),
    OUT_RANGE("R900", "数字超出范围"),

    ADD_SUCCESS("R20001", "添加成功"),
    DELETE_SUCCESS("R20002", "删除成功"),
    UPDATE_SUCCESS("R20003", "更新成功"),
    ;

    /**
     * 返回结果编码
     */
    private final String code;

    /**
     * 返回结果信息
     */
    private final String message;

    /**
     * 根据枚举编码获取枚举
     *
     * @param code 编码
     * @return {@link ResponseEnum}
     */
    public static ResponseEnum ofCode(String code) {
        Optional<ResponseEnum> any = Arrays.stream(ResponseEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据枚举名称获取枚举
     *
     * @param name 枚举名称
     * @return {@link ResponseEnum}
     */
    public static ResponseEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}