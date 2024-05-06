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

package io.github.pnoker.common.driver.metadata;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.pnoker.common.driver.entity.dto.*;
import io.github.pnoker.common.driver.grpc.client.DeviceClient;
import io.github.pnoker.common.entity.bo.AttributeBO;
import io.github.pnoker.common.exception.ConfigException;
import io.github.pnoker.common.utils.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 设备元数据
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class DeviceMetadata {

    /**
     * deviceId,deviceDTO
     */
    private final AsyncLoadingCache<Long, DeviceDTO> cache;

    @Resource
    private DriverMetadata driverMetadata;

    @Resource
    private DeviceClient deviceClient;

    public DeviceMetadata() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(2000)
                .expireAfterWrite(24, TimeUnit.HOURS)
                .removalListener((key, value, cause) -> log.info("Remove device={}, value={} cache, reason is: {}", key, value, cause))
                .buildAsync((rawValue, executor) -> CompletableFuture.supplyAsync(() -> {
                    log.info("Load device metadata by id: {}", rawValue);
                    DeviceDTO deviceDTO = deviceClient.selectById(rawValue);
                    log.info("Cache device metadata: {}", JsonUtil.toJsonString(deviceDTO));
                    return deviceDTO;
                }, executor));
    }

    public void loadAllCache() {
        List<DeviceDTO> entityDTOList = deviceClient.list();
        entityDTOList.forEach(entityDTO -> setCache(entityDTO.getId(), entityDTO));
    }

    public void loadCache(long id) {
        DeviceDTO entityDTO = deviceClient.selectById(id);
        setCache(entityDTO.getId(), entityDTO);
    }

    public void setCache(long id, DeviceDTO deviceDTO) {
        cache.put(id, CompletableFuture.completedFuture(deviceDTO));
    }

    public void removeCache(long id) {
        cache.put(id, CompletableFuture.completedFuture(null));
    }

    public List<DeviceDTO> getAllDevice() {
        List<DeviceDTO> entityDTOList = new ArrayList<>();
        Collection<CompletableFuture<DeviceDTO>> futures = cache.asMap().values();
        for (CompletableFuture<DeviceDTO> future : futures) {
            try {
                DeviceDTO entityDTO = future.get();
                if (ObjectUtil.isNotNull(entityDTO)) {
                    entityDTOList.add(entityDTO);
                }
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                log.error("Failed to get device cache: {}", e.getMessage(), e);
            }
        }
        return entityDTOList;
    }

    public DeviceDTO getDevice(long id) {
        try {
            CompletableFuture<DeviceDTO> future = cache.get(id);
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            log.error("Failed to get device cache: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取驱动属性配置
     * <p>
     * 会校验是否完整
     *
     * @param deviceId 设备ID
     * @return 属性配置Map
     */
    public Map<String, AttributeBO> getDriverAttributeConfig(long deviceId) {
        Map<Long, DriverAttributeDTO> attributeMap = driverMetadata.getDriverAttributeMap();
        if (MapUtil.isEmpty(attributeMap)) {
            return MapUtil.empty();
        }

        DeviceDTO device = getDevice(deviceId);
        if (ObjectUtil.isNull(device)) {
            throw new ConfigException("Failed to get config, the device is empty");
        }

        Map<Long, DriverAttributeConfigDTO> attributeConfigMap = device.getDriverAttributeConfigMap();
        if (MapUtil.isEmpty(attributeConfigMap)) {
            throw new ConfigException("Failed to get config, the driver attribute config is empty");
        }
        if (!attributeConfigMap.keySet().containsAll(attributeMap.keySet())) {
            throw new ConfigException("Failed to get config, the driver attribute config is incomplete");
        }

        return attributeMap.entrySet().stream()
                .collect(Collectors.toMap(
                                entry -> entry.getValue().getAttributeName(),
                                entry -> AttributeBO.builder()
                                        .type(entry.getValue().getAttributeTypeFlag())
                                        .value(attributeConfigMap.get(entry.getKey()).getConfigValue())
                                        .build()
                        )
                );
    }

    /**
     * 获取设备下指定位号属性配置
     * <p>
     * 会校验是否完整
     *
     * @param deviceId 设备ID
     * @param pointId  位号ID
     * @return 属性配置Map
     */
    public Map<String, AttributeBO> getPointAttributeConfig(long deviceId, long pointId) {
        Map<Long, PointAttributeDTO> attributeMap = driverMetadata.getPointAttributeMap();
        if (MapUtil.isEmpty(attributeMap)) {
            return MapUtil.empty();
        }

        DeviceDTO device = getDevice(deviceId);
        if (ObjectUtil.isNull(device)) {
            throw new ConfigException("Failed to get config, the device is empty");
        }

        Map<Long, Map<Long, PointAttributeConfigDTO>> pointAttributeConfigMap = device.getPointAttributeConfigMap();
        if (ObjectUtil.isNull(pointAttributeConfigMap)) {
            throw new ConfigException("Failed to get config, the device point attribute config is empty");
        }

        Map<Long, PointAttributeConfigDTO> attributeConfigMap = pointAttributeConfigMap.get(pointId);
        if (MapUtil.isEmpty(attributeConfigMap)) {
            throw new ConfigException("Failed to get config, the point attribute config is empty");
        }
        if (!attributeConfigMap.keySet().containsAll(attributeMap.keySet())) {
            throw new ConfigException("Failed to get config, the point attribute config is incomplete");
        }

        return attributeMap.entrySet().stream()
                .collect(Collectors.toMap(
                                entry -> entry.getValue().getAttributeName(),
                                entry -> AttributeBO.builder()
                                        .type(entry.getValue().getAttributeTypeFlag())
                                        .value(attributeConfigMap.get(entry.getKey()).getConfigValue())
                                        .build()
                        )
                );
    }

    /**
     * 获取设备下全部位号属性配置
     * <p>
     * 会校验是否完整
     *
     * @param deviceId 设备ID
     * @return 属性配置Map
     */
    public Map<Long, Map<String, AttributeBO>> getPointAttributeConfig(long deviceId) {
        Map<Long, PointAttributeDTO> attributeMap = driverMetadata.getPointAttributeMap();
        if (MapUtil.isEmpty(attributeMap)) {
            return MapUtil.empty();
        }

        DeviceDTO device = getDevice(deviceId);
        if (ObjectUtil.isNull(device)) {
            throw new ConfigException("Failed to get config, the device is empty");
        }

        Map<Long, Map<Long, PointAttributeConfigDTO>> pointAttributeConfigMap = device.getPointAttributeConfigMap();
        if (ObjectUtil.isNull(pointAttributeConfigMap)) {
            throw new ConfigException("Failed to get config, the device point attribute config is empty");
        }

        return pointAttributeConfigMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry1 -> {
            Map<Long, PointAttributeConfigDTO> attributeConfigMap = entry1.getValue();
            if (MapUtil.isEmpty(attributeConfigMap)) {
                throw new ConfigException("Failed to get config, the point attribute config is empty");
            }
            if (!attributeConfigMap.keySet().containsAll(attributeMap.keySet())) {
                throw new ConfigException("Failed to get config, the point attribute config is incomplete");
            }

            return attributeMap.entrySet().stream()
                    .collect(Collectors.toMap(
                                    entry -> entry.getValue().getAttributeName(),
                                    entry -> AttributeBO.builder()
                                            .type(entry.getValue().getAttributeTypeFlag())
                                            .value(attributeConfigMap.get(entry.getKey()).getConfigValue())
                                            .build()
                            )
                    );
        }));
    }

}
