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

package io.github.pnoker.common.entity.builder;

import io.github.pnoker.common.constant.common.ExceptionConstant;
import io.github.pnoker.common.entity.bo.PointValueBO;
import io.github.pnoker.common.entity.model.MgPointValueDO;

import java.util.ArrayList;
import java.util.List;

/**
 * PointValue Builder
 *
 * @author pnoker
 * @since 2022.1.0
 */
public class MgPointValueBuilder {

    private MgPointValueBuilder() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * BO to Mongo DO
     *
     * @param entityBO EntityBO
     * @return EntityDO
     */
    public static MgPointValueDO buildMgDOByBO(PointValueBO entityBO) {
        if (entityBO == null) {
            return null;
        }

        MgPointValueDO mgPointValueDO = new MgPointValueDO();

        mgPointValueDO.setId(entityBO.getId());
        mgPointValueDO.setDeviceId(entityBO.getDeviceId());
        mgPointValueDO.setPointId(entityBO.getPointId());
        mgPointValueDO.setRawValue(entityBO.getRawValue());
        mgPointValueDO.setValue(entityBO.getValue());
        mgPointValueDO.setOriginTime(entityBO.getOriginTime());
        mgPointValueDO.setCreateTime(entityBO.getCreateTime());

        return mgPointValueDO;
    }

    /**
     * Mongo DO to BO
     *
     * @param entityDO EntityDO
     * @return EntityBO
     */
    public static PointValueBO buildBOByMgDO(MgPointValueDO entityDO) {
        if (entityDO == null) {
            return null;
        }

        PointValueBO.PointValueBOBuilder pointValueBO = PointValueBO.builder();

        pointValueBO.id(entityDO.getId());
        pointValueBO.deviceId(entityDO.getDeviceId());
        pointValueBO.pointId(entityDO.getPointId());
        pointValueBO.rawValue(entityDO.getRawValue());
        pointValueBO.value(entityDO.getValue());
        pointValueBO.originTime(entityDO.getOriginTime());
        pointValueBO.createTime(entityDO.getCreateTime());

        return pointValueBO.build();
    }

    /**
     * Mongo DOList to BOList
     *
     * @param entityDOList EntityDO Array
     * @return EntityBO Array
     */
    public static List<PointValueBO> buildBOListByDOList(List<MgPointValueDO> entityDOList) {
        if (entityDOList == null) {
            return null;
        }

        List<PointValueBO> list = new ArrayList<PointValueBO>(entityDOList.size());
        for (MgPointValueDO mgPointValueDO : entityDOList) {
            list.add(buildBOByMgDO(mgPointValueDO));
        }

        return list;
    }

}