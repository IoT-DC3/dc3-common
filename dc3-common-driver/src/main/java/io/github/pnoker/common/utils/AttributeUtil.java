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

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.constant.common.ExceptionConstant;
import io.github.pnoker.common.entity.bo.AttributeBO;
import io.github.pnoker.common.entity.dto.DriverAttributeConfigDTO;
import io.github.pnoker.common.entity.dto.DriverAttributeDTO;
import io.github.pnoker.common.entity.dto.PointAttributeConfigDTO;
import io.github.pnoker.common.entity.dto.PointAttributeDTO;
import io.github.pnoker.common.enums.AttributeTypeFlagEnum;
import io.github.pnoker.common.exception.ConfigException;
import io.github.pnoker.common.exception.EmptyException;
import io.github.pnoker.common.exception.TypeException;
import io.github.pnoker.common.exception.UnSupportException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
     * @param attributeConfig Attribute Config {@link AttributeBO}
     * @param <T>             T
     * @return T
     */
    public static <T> T getAttributeValue(AttributeBO attributeConfig, Class<T> clazz) {
        return value(attributeConfig.getType(), attributeConfig.getValue(), clazz);
    }

    /**
     * 获取驱动属性配置
     * <p>
     * 会校验是否完整
     *
     * @param attributeMap       驱动属性
     * @param attributeConfigMap 驱动属性配置
     * @return 属性配置Map
     */
    public static Map<String, AttributeBO> getDriverAttributeConfig(Map<Long, DriverAttributeDTO> attributeMap, Map<Long, DriverAttributeConfigDTO> attributeConfigMap) {
        if (!isDriverAttributeComplete(attributeMap, attributeConfigMap)) {
            throw new ConfigException("Failed to get config, the driver attribute config is incomplete");
        }

        Map<String, AttributeBO> configMap = new HashMap<>();
        if (MapUtil.isEmpty(attributeMap)) {
            return configMap;
        }

        for (Map.Entry<Long, DriverAttributeDTO> entry : attributeMap.entrySet()) {
            DriverAttributeDTO attribute = entry.getValue();
            DriverAttributeConfigDTO config = attributeConfigMap.get(entry.getKey());
            AttributeBO entityBO = new AttributeBO();
            entityBO.setType(attribute.getAttributeTypeFlag());
            entityBO.setValue(config.getConfigValue());
            configMap.put(attribute.getAttributeName(), entityBO);
        }

        return configMap;
    }

    /**
     * 获取位号属性配置
     * <p>
     * 会校验是否完整
     *
     * @param attributeMap       位号属性
     * @param attributeConfigMap 位号属性配置
     * @return 属性配置Map
     */
    public static Map<String, AttributeBO> getPointAttributeConfig(Map<Long, PointAttributeDTO> attributeMap, Map<Long, PointAttributeConfigDTO> attributeConfigMap) {
        if (!isPointAttributeComplete(attributeMap, attributeConfigMap)) {
            throw new ConfigException("Failed to get config, the point attribute config is incomplete");
        }

        Map<String, AttributeBO> configMap = new HashMap<>();
        if (MapUtil.isEmpty(attributeMap)) {
            return configMap;
        }

        for (Map.Entry<Long, PointAttributeDTO> entry : attributeMap.entrySet()) {
            PointAttributeDTO attribute = entry.getValue();
            PointAttributeConfigDTO config = attributeConfigMap.get(entry.getKey());
            AttributeBO entityBO = new AttributeBO();
            entityBO.setType(attribute.getAttributeTypeFlag());
            entityBO.setValue(config.getConfigValue());
            configMap.put(attribute.getAttributeName(), entityBO);
        }

        return configMap;
    }

    /**
     * 根据类型转换数据
     *
     * @param attributeType  属性类型 {@link  AttributeTypeFlagEnum}
     * @param attributeValue 属性值
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

    /**
     * 校验驱动属性配置是否完整
     *
     * @param attributeMap       驱动属性
     * @param attributeConfigMap 驱动属性配置
     * @return 是否完整
     */
    private static boolean isDriverAttributeComplete(Map<Long, DriverAttributeDTO> attributeMap, Map<Long, DriverAttributeConfigDTO> attributeConfigMap) {
        if (MapUtil.isEmpty(attributeMap)) {
            return true;
        }
        if (MapUtil.isEmpty(attributeConfigMap)) {
            return false;
        }

        Set<Long> attributeIds = attributeMap.keySet();
        Set<Long> attributeConfigIds = attributeConfigMap.keySet();
        return attributeConfigIds.containsAll(attributeIds);
    }

    /**
     * 校验位号属性配置是否完整
     *
     * @param attributeMap       位号属性
     * @param attributeConfigMap 位号属性配置
     * @return 是否完整
     */
    private static boolean isPointAttributeComplete(Map<Long, PointAttributeDTO> attributeMap, Map<Long, PointAttributeConfigDTO> attributeConfigMap) {
        if (MapUtil.isEmpty(attributeMap)) {
            return true;
        }
        if (MapUtil.isEmpty(attributeConfigMap)) {
            return false;
        }

        Set<Long> attributeIds = attributeMap.keySet();
        Set<Long> attributeConfigIds = attributeConfigMap.keySet();
        return attributeConfigIds.containsAll(attributeIds);
    }
}
