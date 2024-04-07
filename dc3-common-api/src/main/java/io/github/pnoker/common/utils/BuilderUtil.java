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

import io.github.pnoker.api.common.GrpcBase;
import io.github.pnoker.common.constant.common.ExceptionConstant;
import io.github.pnoker.common.constant.common.TimeConstant;
import io.github.pnoker.common.entity.base.BaseBO;

/**
 * Builder 工具类
 *
 * @author pnoker
 * @since 2022.1.0
 */
public class BuilderUtil {

    private BuilderUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    public static GrpcBase buildBaseDTOByDO(BaseBO entityBO) {
        GrpcBase.Builder builder = GrpcBase.newBuilder();
        builder.setId(entityBO.getId());
        builder.setRemark(entityBO.getRemark());
        builder.setCreatorId(entityBO.getCreatorId());
        builder.setCreatorName(entityBO.getCreatorName());
        builder.setCreateTime(entityBO.getCreateTime().atZone(TimeConstant.DEFAULT_ZONEID).toInstant().toEpochMilli());
        builder.setOperatorId(entityBO.getOperatorId());
        builder.setOperatorName(entityBO.getOperatorName());
        builder.setOperateTime(entityBO.getOperateTime().atZone(TimeConstant.DEFAULT_ZONEID).toInstant().toEpochMilli());
        return builder.build();
    }
}
