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

package io.github.pnoker.common.utils;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.constant.common.ExceptionConstant;
import io.github.pnoker.common.entity.dto.AttributeConfigDTO;
import io.github.pnoker.common.enums.AttributeTypeFlagEnum;
import io.github.pnoker.common.exception.EmptyException;
import io.github.pnoker.common.exception.TypeException;
import io.github.pnoker.common.exception.UnSupportException;
import lombok.extern.slf4j.Slf4j;

/**
 * 属性工具类
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
public class AttributeUtil {

    private AttributeUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 获取 属性值
     *
     * @param attributeConfig Attribute Config {@link AttributeConfigDTO}
     * @param <T>             T
     * @return T
     */
    public static <T> T getAttributeValue(AttributeConfigDTO attributeConfig, Class<T> clazz) {
        return value(attributeConfig.getType(), attributeConfig.getValue(), clazz);
    }

    /**
     * 通过类型转换数据
     *
     * @param attributeType  Attribute Type {@link  AttributeTypeFlagEnum}
     * @param attributeValue Attribute Value
     * @param clazz          T Class
     * @param <T>            T
     * @return T
     */
    @SuppressWarnings("unchecked")
    private static <T> T value(AttributeTypeFlagEnum attributeType, String attributeValue, Class<T> clazz) {
        if (ObjectUtil.isNull(attributeType)) {
            throw new UnSupportException("Unsupported attribute type of " + attributeType);
        }
        if (CharSequenceUtil.isEmpty(attributeValue)) {
            throw new EmptyException("Attribute value is empty");
        }

        final String message = "Attribute type is: {}, can't be cast to class: {}";
        return switch (attributeType) {
            case STRING -> {
                if (!clazz.equals(String.class)) {
                    throw new TypeException(message, attributeType.getCode(), clazz.getName());
                }
                yield (T) attributeValue;
            }
            case BYTE -> {
                if (!clazz.equals(Byte.class)) {
                    throw new TypeException(message, attributeType.getCode(), clazz.getName());
                }
                yield (T) Byte.valueOf(attributeValue);
            }
            case SHORT -> {
                if (!clazz.equals(Short.class)) {
                    throw new TypeException(message, attributeType.getCode(), clazz.getName());
                }
                yield (T) Short.valueOf(attributeValue);
            }
            case INT -> {
                if (!clazz.equals(Integer.class)) {
                    throw new TypeException(message, attributeType.getCode(), clazz.getName());
                }
                yield (T) Integer.valueOf(attributeValue);
            }
            case LONG -> {
                if (!clazz.equals(Long.class)) {
                    throw new TypeException(message, attributeType.getCode(), clazz.getName());
                }
                yield (T) Long.valueOf(attributeValue);
            }
            case FLOAT -> {
                if (!clazz.equals(Float.class)) {
                    throw new TypeException(message, attributeType.getCode(), clazz.getName());
                }
                yield (T) Float.valueOf(attributeValue);
            }
            case DOUBLE -> {
                if (!clazz.equals(Double.class)) {
                    throw new TypeException(message, attributeType.getCode(), clazz.getName());
                }
                yield (T) Double.valueOf(attributeValue);
            }
            case BOOLEAN -> {
                if (!clazz.equals(Boolean.class)) {
                    throw new TypeException(message, attributeType.getCode(), clazz.getName());
                }
                yield (T) Boolean.valueOf(attributeValue);
            }
        };
    }
}
