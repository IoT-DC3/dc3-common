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

package io.github.pnoker.common.driver.service.impl;


import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.pnoker.api.common.driver.DeviceApiGrpc;
import io.github.pnoker.api.common.driver.GrpcDeviceQuery;
import io.github.pnoker.api.common.driver.GrpcRDeviceDTO;
import io.github.pnoker.common.constant.service.ManagerConstant;
import io.github.pnoker.common.driver.entity.builder.DeviceBuilder;
import io.github.pnoker.common.driver.service.DeviceMetadataService;
import io.github.pnoker.common.entity.dto.DeviceDTO;
import io.github.pnoker.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DeviceMetadataServiceImpl implements DeviceMetadataService {

    @GrpcClient(ManagerConstant.SERVICE_NAME)
    private DeviceApiGrpc.DeviceApiBlockingStub deviceApiBlockingStub;

    private final DeviceBuilder deviceBuilder;
    private final AsyncLoadingCache<Long, DeviceDTO> cache;

    public DeviceMetadataServiceImpl(DeviceBuilder deviceBuilder) {
        this.deviceBuilder = deviceBuilder;
        this.cache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(24, TimeUnit.HOURS)
                .removalListener((key, value, cause) -> log.info("Remove key={}, value={} cache, reason is: {}", key, value, cause))
                .buildAsync((rawValue, executor) -> CompletableFuture.supplyAsync(() -> {
                    log.info("Load device metadata by id: {}", rawValue);
                    DeviceDTO deviceDTO = selectById(rawValue);
                    log.info("Cache device metadata: {}", JsonUtil.toJsonString(deviceDTO));
                    return deviceDTO;
                }, executor));
    }

    @Override
    public DeviceDTO selectById(Long id) {
        GrpcDeviceQuery.Builder query = GrpcDeviceQuery.newBuilder();
        query.setDeviceId(id);
        GrpcRDeviceDTO rDeviceDTO = deviceApiBlockingStub.selectById(query.build());
        if (!rDeviceDTO.getResult().getOk()) {
            log.error("Device doesn't exist: {}", id);
            return null;
        }

        return deviceBuilder.buildDTOByGrpcDTO(rDeviceDTO.getData());
    }
}
