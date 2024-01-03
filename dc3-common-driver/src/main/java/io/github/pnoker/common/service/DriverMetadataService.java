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

package io.github.pnoker.common.service;

import io.github.pnoker.common.entity.dto.DriverTransferMetadataDTO;

/**
 * 驱动元数据相关接口
 *
 * @author pnoker
 * @since 2022.1.0
 */
public interface DriverMetadataService {

    /**
     * 模板元数据
     *
     * @param entityDTO DriverMetadataDTO
     */
    void profileMetadata(DriverTransferMetadataDTO entityDTO);

    /**
     * 设备元数据
     *
     * @param entityDTO DriverMetadataDTO
     */
    void deviceMetadata(DriverTransferMetadataDTO entityDTO);

    /**
     * 位号元数据
     *
     * @param entityDTO DriverMetadataDTO
     */
    void pointMetadata(DriverTransferMetadataDTO entityDTO);

    /**
     * 驱动配置元数据
     *
     * @param entityDTO DriverMetadataDTO
     */
    void driverInfoMetadata(DriverTransferMetadataDTO entityDTO);

    /**
     * 位号配置元数据
     *
     * @param entityDTO DriverMetadataDTO
     */
    void pointInfoMetadata(DriverTransferMetadataDTO entityDTO);
}
