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

package io.github.pnoker.common.driver.grpc.client;

import io.github.pnoker.api.common.GrpcDriverAttributeDTO;
import io.github.pnoker.api.common.GrpcDriverDTO;
import io.github.pnoker.api.common.GrpcPointAttributeDTO;
import io.github.pnoker.api.common.driver.DriverApiGrpc;
import io.github.pnoker.api.common.driver.GrpcDriverRegisterDTO;
import io.github.pnoker.api.common.driver.GrpcRDriverRegisterDTO;
import io.github.pnoker.common.constant.service.ManagerConstant;
import io.github.pnoker.common.driver.entity.builder.GrpcDriverAttributeBuilder;
import io.github.pnoker.common.driver.entity.builder.GrpcDriverBuilder;
import io.github.pnoker.common.driver.entity.builder.GrpcPointAttributeBuilder;
import io.github.pnoker.common.driver.entity.dto.DriverAttributeDTO;
import io.github.pnoker.common.driver.entity.dto.DriverDTO;
import io.github.pnoker.common.driver.entity.dto.DriverRegisterDTO;
import io.github.pnoker.common.driver.entity.dto.PointAttributeDTO;
import io.github.pnoker.common.driver.metadata.DriverMetadata;
import io.github.pnoker.common.enums.DriverStatusEnum;
import io.github.pnoker.common.exception.ServiceException;
import io.github.pnoker.common.optional.CollectionOptional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DriverClient {

    @GrpcClient(ManagerConstant.SERVICE_NAME)
    private DriverApiGrpc.DriverApiBlockingStub driverApiBlockingStub;

    @Resource
    private DriverMetadata driverMetadata;

    @Resource
    private GrpcDriverBuilder grpcDriverBuilder;
    @Resource
    private GrpcDriverAttributeBuilder grpcDriverAttributeBuilder;
    @Resource
    private GrpcPointAttributeBuilder grpcPointAttributeBuilder;

    /**
     * 根据 位号ID 获取位号元数据
     *
     * @param entityDTO DriverRegisterDTO
     */
    public void driverRegister(DriverRegisterDTO entityDTO) {

        GrpcDriverRegisterDTO.Builder builder = GrpcDriverRegisterDTO.newBuilder();
        GrpcDriverDTO grpcDriverDTO = grpcDriverBuilder.buildGrpcDTOByDTO(entityDTO.getDriver());
        builder.setTenant(entityDTO.getTenant())
                .setClient(entityDTO.getClient())
                .setDriver(grpcDriverDTO);

        CollectionOptional.ofNullable(entityDTO.getDriverAttributes()).ifPresent(value -> {
                    List<GrpcDriverAttributeDTO> grpcDriverAttributeDTOList = value.stream().map(grpcDriverAttributeBuilder::buildGrpcDTOByDTO).toList();
                    builder.addAllDriverAttributes(grpcDriverAttributeDTOList);
                }
        );
        CollectionOptional.ofNullable(entityDTO.getPointAttributes()).ifPresent(value -> {
                    List<GrpcPointAttributeDTO> grpcPointAttributeDTOList = value.stream().map(grpcPointAttributeBuilder::buildGrpcDTOByDTO).toList();
                    builder.addAllPointAttributes(grpcPointAttributeDTOList);
                }
        );

        GrpcRDriverRegisterDTO rDriverRegisterDTO = driverApiBlockingStub.driverRegister(builder.build());
        if (!rDriverRegisterDTO.getResult().getOk()) {
            throw new ServiceException(rDriverRegisterDTO.getResult().getMessage());
        }

        DriverDTO driverDTO = grpcDriverBuilder.buildDTOByGrpcDTO(rDriverRegisterDTO.getDriver());
        driverMetadata.setDriver(driverDTO);

        List<GrpcDriverAttributeDTO> driverAttributesList = rDriverRegisterDTO.getDriverAttributesList();
        Map<Long, DriverAttributeDTO> driverAttributeDTOMap = driverAttributesList.stream().collect(Collectors.toMap(entity -> entity.getBase().getId(), grpcDriverAttributeBuilder::buildDTOByGrpcDTO));
        driverMetadata.setDriverAttributeMap(driverAttributeDTOMap);


        List<GrpcPointAttributeDTO> pointAttributesList = rDriverRegisterDTO.getPointAttributesList();
        Map<Long, PointAttributeDTO> pointAttributeDTOMap = pointAttributesList.stream().collect(Collectors.toMap(entity -> entity.getBase().getId(), grpcPointAttributeBuilder::buildDTOByGrpcDTO));
        driverMetadata.setPointAttributeMap(pointAttributeDTOMap);

        driverMetadata.setDriverStatus(DriverStatusEnum.ONLINE);
    }
}
