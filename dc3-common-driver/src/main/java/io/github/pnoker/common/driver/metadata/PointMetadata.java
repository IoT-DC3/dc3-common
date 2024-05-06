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


import cn.hutool.core.util.ObjectUtil;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.pnoker.common.driver.entity.dto.PointDTO;
import io.github.pnoker.common.driver.grpc.client.PointClient;
import io.github.pnoker.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 位号元数据
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class PointMetadata {

    /**
     * pointId,pointDTO
     */
    private final AsyncLoadingCache<Long, PointDTO> cache;

    private final PointClient pointClient;

    public PointMetadata(PointClient pointClient) {
        this.pointClient = pointClient;
        this.cache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(24, TimeUnit.HOURS)
                .removalListener((key, value, cause) -> log.info("Remove point={}, value={} cache, reason is: {}", key, value, cause))
                .buildAsync((rawValue, executor) -> CompletableFuture.supplyAsync(() -> {
                    log.info("Load point metadata by id: {}", rawValue);
                    PointDTO pointDTO = this.pointClient.selectById(rawValue);
                    log.info("Cache point metadata: {}", JsonUtil.toJsonString(pointDTO));
                    return pointDTO;
                }, executor));
    }

    public void loadAllCache() {
        List<PointDTO> entityDTOList = pointClient.list();
        entityDTOList.forEach(entityDTO -> setCache(entityDTO.getId(), entityDTO));
    }

    public List<PointDTO> getAllCache() {
        List<PointDTO> entityDTOList = new ArrayList<>();
        Collection<CompletableFuture<PointDTO>> futures = cache.asMap().values();
        for (CompletableFuture<PointDTO> future : futures) {
            try {
                PointDTO entityDTO = future.get();
                if (ObjectUtil.isNotNull(entityDTO)) {
                    entityDTOList.add(entityDTO);
                }
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                log.error("Failed to get point cache: {}", e.getMessage(), e);
            }
        }
        return entityDTOList;
    }

    public PointDTO getCache(long id) {
        try {
            CompletableFuture<PointDTO> future = cache.get(id);
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            log.error("Failed to get point cache: {}", e.getMessage(), e);
            return null;
        }
    }

    public void setCache(long id, PointDTO pointDTO) {
        cache.put(id, CompletableFuture.completedFuture(pointDTO));
    }

    public void removeCache(long id) {
        cache.put(id, CompletableFuture.completedFuture(null));
    }
}
