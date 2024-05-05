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

package io.github.pnoker.common.driver.entity.builder;

import io.github.pnoker.api.common.GrpcDeviceDTO;
import io.github.pnoker.common.entity.dto.DeviceDTO;
import io.github.pnoker.common.entity.ext.DeviceExt;
import io.github.pnoker.common.optional.CollectionOptional;
import io.github.pnoker.common.optional.EnableOptional;
import io.github.pnoker.common.optional.JsonOptional;
import io.github.pnoker.common.utils.GrpcBuilderUtil;
import io.github.pnoker.common.utils.JsonUtil;
import io.github.pnoker.common.utils.MapStructUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.HashSet;

/**
 * Device Builder
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Mapper(componentModel = "spring", uses = {MapStructUtil.class})
public interface GrpcDeviceBuilder {

    @Mapping(target = "deviceExt", ignore = true)
    @Mapping(target = "enableFlag", ignore = true)
    @Mapping(target = "profileIds", ignore = true)
    @Mapping(target = "driverAttributeConfigMap", ignore = true)
    @Mapping(target = "pointAttributeConfigMap", ignore = true)
    DeviceDTO buildDTOByGrpcDTO(GrpcDeviceDTO entityGrpc);

    @AfterMapping
    default void afterProcess(GrpcDeviceDTO entityGrpc, @MappingTarget DeviceDTO entityDTO) {
        GrpcBuilderUtil.buildBaseDTOByGrpcBase(entityGrpc.getBase(), entityDTO);

        CollectionOptional.ofNullable(entityGrpc.getProfileIdsList()).ifPresent(value -> entityDTO.setProfileIds(new HashSet<>(value)));
        JsonOptional.ofNullable(entityGrpc.getDeviceExt()).ifPresent(value -> entityDTO.setDeviceExt(JsonUtil.parseObject(value, DeviceExt.class)));
        EnableOptional.ofNullable(entityGrpc.getEnableFlag()).ifPresent(entityDTO::setEnableFlag);
    }
}