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

import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.api.common.GrpcBase;
import io.github.pnoker.api.common.GrpcPage;
import io.github.pnoker.common.constant.common.DefaultConstant;
import io.github.pnoker.common.constant.common.ExceptionConstant;
import io.github.pnoker.common.entity.base.BaseBO;
import io.github.pnoker.common.entity.base.BaseDTO;
import io.github.pnoker.common.entity.common.Pages;

import java.util.Optional;

/**
 * Grpc Builder 工具类
 *
 * @author pnoker
 * @since 2022.1.0
 */
public class GrpcBuilderUtil {

    private GrpcBuilderUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * Entity Base BO to Grpc Base DTO
     *
     * @param entityBO EntityBO
     * @param <T>      EntityBO extends BaseBO
     * @return GrpcBase
     */
    public static <T extends BaseBO> GrpcBase buildGrpcBaseByBO(T entityBO) {
        if (ObjectUtil.isNull(entityBO)) {
            return null;
        }

        GrpcBase.Builder builder = GrpcBase.newBuilder();
        Optional.ofNullable(entityBO.getId()).ifPresentOrElse(builder::setId, () -> builder.setId(DefaultConstant.DEFAULT_NULL_INT_VALUE));
        Optional.ofNullable(entityBO.getRemark()).ifPresent(builder::setRemark);
        Optional.ofNullable(entityBO.getCreatorId()).ifPresentOrElse(builder::setCreatorId, () -> builder.setCreatorId(DefaultConstant.DEFAULT_NULL_INT_VALUE));
        Optional.ofNullable(entityBO.getCreatorName()).ifPresent(builder::setCreatorName);
        Optional.ofNullable(entityBO.getCreateTime()).ifPresent(time -> builder.setCreateTime(LocalDateTimeUtil.milliSeconds(time)));
        Optional.ofNullable(entityBO.getOperatorId()).ifPresentOrElse(builder::setOperatorId, () -> builder.setOperatorId(DefaultConstant.DEFAULT_NULL_INT_VALUE));
        Optional.ofNullable(entityBO.getOperatorName()).ifPresent(builder::setOperatorName);
        Optional.ofNullable(entityBO.getOperateTime()).ifPresent(time -> builder.setOperateTime(LocalDateTimeUtil.milliSeconds(time)));
        return builder.build();
    }

    /**
     * Grpc Base to Base BO
     *
     * @param entityGrpc GrpcBase
     * @param entityBO   EntityBO
     * @param <T>        EntityBO extends BaseBO
     */
    public static <T extends BaseBO> void buildBaseBOByGrpcBase(GrpcBase entityGrpc, T entityBO) {
        if (ObjectUtil.isNull(entityGrpc)) {
            return;
        }

        entityBO.setId(entityGrpc.getId());
        entityBO.setRemark(entityGrpc.getRemark());
        entityBO.setCreatorId(entityGrpc.getCreatorId());
        entityBO.setCreatorName(entityGrpc.getCreatorName());
        entityBO.setCreateTime(LocalDateTimeUtil.localDateTime(entityGrpc.getCreateTime()));
        entityBO.setOperatorId(entityGrpc.getOperatorId());
        entityBO.setOperatorName(entityGrpc.getOperatorName());
        entityBO.setOperateTime(LocalDateTimeUtil.localDateTime(entityGrpc.getOperateTime()));
    }

    /**
     * Grpc Base to Base DTO
     *
     * @param entityGrpc GrpcBase
     * @param entityDTO  EntityDTO
     * @param <T>        EntityDTO extends GrpcBase
     */
    public static <T extends BaseDTO> void buildBaseDTOByGrpcBase(GrpcBase entityGrpc, T entityDTO) {
        if (ObjectUtil.isNull(entityGrpc)) {
            return;
        }

        entityDTO.setId(entityGrpc.getId());
        entityDTO.setRemark(entityGrpc.getRemark());
        entityDTO.setCreatorId(entityGrpc.getCreatorId());
        entityDTO.setCreatorName(entityGrpc.getCreatorName());
        entityDTO.setCreateTime(LocalDateTimeUtil.localDateTime(entityGrpc.getCreateTime()));
        entityDTO.setOperatorId(entityGrpc.getOperatorId());
        entityDTO.setOperatorName(entityGrpc.getOperatorName());
        entityDTO.setOperateTime(LocalDateTimeUtil.localDateTime(entityGrpc.getOperateTime()));
    }

    /**
     * Grpc Page to Pages
     *
     * @param page GrpcPage
     * @return Pages
     */
    public static Pages buildPagesByGrpcPage(GrpcPage page) {
        if (ObjectUtil.isNull(page)) {
            GrpcPage.Builder builder = GrpcPage.newBuilder();
            builder.setCurrent(1);
            builder.setPages(DefaultConstant.DEFAULT_PAGE_SIZE);
            page = builder.build();
        }

        Pages pages = new Pages();
        long current = page.getCurrent() < 1 ? 1 : page.getCurrent();
        long pageSize = page.getSize() < 1 ? DefaultConstant.DEFAULT_PAGE_SIZE : page.getSize();
        pageSize = pageSize > DefaultConstant.DEFAULT_MAX_PAGE_SIZE ? DefaultConstant.DEFAULT_MAX_PAGE_SIZE : pageSize;
        pages.setCurrent(current);
        pages.setSize(pageSize);
        return pages;
    }

}
