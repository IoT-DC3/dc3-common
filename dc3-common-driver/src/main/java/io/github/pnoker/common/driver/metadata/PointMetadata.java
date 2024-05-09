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
import io.github.pnoker.common.driver.entity.bo.PointBO;
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
    private final AsyncLoadingCache<Long, PointBO> cache;

    private final PointClient pointClient;

    public PointMetadata(PointClient pointClient) {
        this.pointClient = pointClient;
        this.cache = Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(24, TimeUnit.HOURS)
                .removalListener((key, value, cause) -> log.info("Remove key={}, value={} cache, reason is: {}", key, value, cause))
                .buildAsync((key, executor) -> CompletableFuture.supplyAsync(() -> {
                    log.info("Load point metadata by id: {}", key);
                    PointBO pointBO = this.pointClient.selectById(key);
                    log.info("Cache point metadata: {}", JsonUtil.toJsonString(pointBO));
                    return pointBO;
                }, executor));
    }

    public void loadAllCache() {
        List<PointBO> entityDTOList = pointClient.list();
        entityDTOList.forEach(entityDTO -> setCache(entityDTO.getId(), entityDTO));
    }

    /**
     * 加载缓存
     */
    public void loadCache(long id) {
        PointBO entityDTO = pointClient.selectById(id);
        setCache(entityDTO.getId(), entityDTO);
    }

    public void setCache(long id, PointBO pointBO) {
        cache.put(id, CompletableFuture.completedFuture(pointBO));
    }

    public void removeCache(long id) {
        cache.put(id, CompletableFuture.completedFuture(null));
    }

    public List<PointBO> getAllPoint() {
        List<PointBO> entityDTOList = new ArrayList<>();
        Collection<CompletableFuture<PointBO>> futures = cache.asMap().values();
        for (CompletableFuture<PointBO> future : futures) {
            try {
                PointBO entityDTO = future.get();
                if (ObjectUtil.isNotNull(entityDTO)) {
                    entityDTOList.add(entityDTO);
                }
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                log.error("Failed to get all point cache: {}", e.getMessage(), e);
            }
        }
        return entityDTOList;
    }

    public PointBO getPoint(long id) {
        try {
            CompletableFuture<PointBO> future = cache.get(id);
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            log.error("Failed to get the point cache: {}", e.getMessage(), e);
            return null;
        }
    }
}
