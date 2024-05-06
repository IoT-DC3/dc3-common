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

import io.github.pnoker.api.common.GrpcBase;
import io.github.pnoker.api.common.GrpcDriverDTO;
import io.github.pnoker.common.constant.common.DefaultConstant;
import io.github.pnoker.common.driver.entity.dto.DriverDTO;
import io.github.pnoker.common.entity.ext.DriverExt;
import io.github.pnoker.common.enums.DriverTypeFlagEnum;
import io.github.pnoker.common.optional.EnableOptional;
import io.github.pnoker.common.optional.JsonOptional;
import io.github.pnoker.common.utils.GrpcBuilderUtil;
import io.github.pnoker.common.utils.JsonUtil;
import io.github.pnoker.common.utils.MapStructUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;

/**
 * Driver Builder
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Mapper(componentModel = "spring", uses = {MapStructUtil.class})
public interface GrpcDriverBuilder {

    @Mapping(target = "driverExt", ignore = true)
    @Mapping(target = "driverTypeFlag", ignore = true)
    @Mapping(target = "enableFlag", ignore = true)
    DriverDTO buildDTOByGrpcDTO(GrpcDriverDTO entityGrpc);

    @AfterMapping
    default void afterProcess(GrpcDriverDTO entityGrpc, @MappingTarget DriverDTO entityDTO) {
        GrpcBuilderUtil.buildBaseDTOByGrpcBase(entityGrpc.getBase(), entityDTO);

        JsonOptional.ofNullable(entityGrpc.getDriverExt()).ifPresent(value -> entityDTO.setDriverExt(JsonUtil.parseObject(value, DriverExt.class)));
        Optional.ofNullable(DriverTypeFlagEnum.ofIndex((byte) entityGrpc.getDriverTypeFlag())).ifPresent(entityDTO::setDriverTypeFlag);
        EnableOptional.ofNullable(entityGrpc.getEnableFlag()).ifPresent(entityDTO::setEnableFlag);
    }


    @Mapping(target = "driverExt", ignore = true)
    @Mapping(target = "driverTypeFlag", ignore = true)
    @Mapping(target = "enableFlag", ignore = true)
    GrpcDriverDTO buildGrpcDTOByDTO(DriverDTO entityDTO);

    @AfterMapping
    default void afterProcess(DriverDTO entityDTO, @MappingTarget GrpcDriverDTO.Builder entityGrpc) {
        GrpcBase grpcBase = GrpcBuilderUtil.buildGrpcBaseByDTO(entityDTO);
        entityGrpc.setBase(grpcBase);

        Optional.ofNullable(entityDTO.getDriverExt()).ifPresent(value -> entityGrpc.setDriverExt(JsonUtil.toJsonString(value)));
        Optional.ofNullable(entityDTO.getDriverTypeFlag()).ifPresentOrElse(value -> entityGrpc.setDriverTypeFlag(value.getIndex()), () -> entityGrpc.setDriverTypeFlag(DefaultConstant.DEFAULT_NULL_INT_VALUE));
        Optional.ofNullable(entityDTO.getEnableFlag()).ifPresentOrElse(value -> entityGrpc.setEnableFlag(value.getIndex()), () -> entityGrpc.setEnableFlag(DefaultConstant.DEFAULT_NULL_INT_VALUE));
    }
}