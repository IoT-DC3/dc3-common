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
     * @param entityBO BaseBO
     * @return GrpcBase
     */
    public static GrpcBase buildGrpcBaseByBO(BaseBO entityBO) {
        if (ObjectUtil.isNull(entityBO)) {
            return null;
        }

        GrpcBase.Builder builder = GrpcBase.newBuilder();
        Optional.of(entityBO.getId()).ifPresent(builder::setId);
        Optional.of(entityBO.getRemark()).ifPresent(builder::setRemark);
        Optional.of(entityBO.getCreatorId()).ifPresent(builder::setCreatorId);
        Optional.of(entityBO.getCreatorName()).ifPresent(builder::setCreatorName);
        Optional.of(entityBO.getCreateTime()).ifPresent(time -> builder.setCreateTime(LocalDateTimeUtil.milliSeconds(time)));
        Optional.of(entityBO.getOperatorId()).ifPresent(builder::setOperatorId);
        Optional.of(entityBO.getOperatorName()).ifPresent(builder::setOperatorName);
        Optional.of(entityBO.getOperateTime()).ifPresent(time -> builder.setOperateTime(LocalDateTimeUtil.milliSeconds(time)));
        return builder.build();
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
