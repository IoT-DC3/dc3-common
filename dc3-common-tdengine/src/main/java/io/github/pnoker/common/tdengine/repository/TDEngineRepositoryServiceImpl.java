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

package io.github.pnoker.common.tdengine.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.pnoker.common.constant.driver.StrategyConstant;
import io.github.pnoker.common.entity.bo.PointValueBO;
import io.github.pnoker.common.entity.common.Pages;
import io.github.pnoker.common.entity.query.PointValueQuery;
import io.github.pnoker.common.repository.RepositoryService;
import io.github.pnoker.common.strategy.RepositoryStrategyFactory;
import io.github.pnoker.common.tdengine.entity.builder.TDEnginePointValueBuilder;
import io.github.pnoker.common.tdengine.entity.model.TDEnginePointValueDO;
import io.github.pnoker.common.tdengine.mapper.TDEngineRepositoryMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service("tdEngineRepositoryService")
public class TDEngineRepositoryServiceImpl implements RepositoryService, InitializingBean {

    @Resource
    private TDEnginePointValueBuilder tdEnginePointValueBuilder;

    @Resource
    private TDEngineRepositoryMapper tdEngineRepositoryMapper;

    private final String stable = "device_point_data";

    @Override
    public String getRepositoryName() {
        return StrategyConstant.Storage.TDENGINE;
    }

    @Override
    public void savePointValue(PointValueBO entityBO) {
        if (!ObjectUtil.isAllNotEmpty(entityBO.getDeviceId(), entityBO.getPointId())) {
            return;
        }
        String tableName = stable + entityBO.getDeviceId();
        TDEnginePointValueDO tdEnginePointValueDO = tdEnginePointValueBuilder.buildMgDOByBO(entityBO);
        tdEngineRepositoryMapper.savePointValue(tableName, tdEnginePointValueDO);
    }

    @Override
    public void savePointValue(Long deviceId, List<PointValueBO> entityBOS) {
        if (ObjectUtil.isEmpty(deviceId)) {
            return;
        }
        String tableName = stable + deviceId;
        List<TDEnginePointValueDO> tdEnginePointValueDOS = tdEnginePointValueBuilder.buildMgDOListByBOList(entityBOS);
        tdEngineRepositoryMapper.saveBatchPointValue(tableName, tdEnginePointValueDOS);
    }

    @Override
    public List<String> selectHistoryPointValue(Long deviceId, Long pointId, int count) {
        List<TDEnginePointValueDO> result = tdEngineRepositoryMapper.selectHistoryPointValue(deviceId, pointId, count);
        return result.stream().map(TDEnginePointValueDO::getValue).toList();
    }

    @Override
    public List<PointValueBO> selectLatestPointValue(Long deviceId, List<Long> pointIds) {
        if (CollUtil.isEmpty(pointIds)) {
            return Collections.emptyList();
        }
        List<TDEnginePointValueDO> tdEnginePointValueDOS = tdEngineRepositoryMapper.selectLatestPointValue(deviceId, pointIds);
        return tdEnginePointValueBuilder.buildBOListByDOList(tdEnginePointValueDOS);
    }

    @Override
    public Page<PointValueBO> selectPagePointValue(PointValueQuery entityQuery) {
        if (ObjectUtil.isEmpty(entityQuery.getPage())) {
            entityQuery.setPage(new Pages());
        }
        Page<PointValueBO> entityPageBO = new Page<>();
        Pages pages = entityQuery.getPage();
        long count = tdEngineRepositoryMapper.count(entityQuery);
        pages.setCurrent((pages.getCurrent() - 1) * pages.getSize());
        List<TDEnginePointValueDO> pointValueDOS = tdEngineRepositoryMapper.selectPagePointValue(entityQuery, pages);
        for (TDEnginePointValueDO pointValueDO : pointValueDOS) {
            pointValueDO.setCreateTime(pointValueDO.getTs().toLocalDateTime());
        }
        List<PointValueBO> pointValueBOS = tdEnginePointValueBuilder.buildBOListByDOList(pointValueDOS);
        entityPageBO.setCurrent(pages.getCurrent()).setSize(pages.getSize()).setTotal(count).setRecords(pointValueBOS);
        return entityPageBO;
    }

    @Override
    public void afterPropertiesSet() {
        RepositoryStrategyFactory.put(StrategyConstant.Storage.TDENGINE, this);
    }

}