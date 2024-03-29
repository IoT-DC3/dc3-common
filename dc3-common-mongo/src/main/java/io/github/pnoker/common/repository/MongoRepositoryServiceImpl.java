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

package io.github.pnoker.common.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.pnoker.common.constant.common.PrefixConstant;
import io.github.pnoker.common.constant.common.SuffixConstant;
import io.github.pnoker.common.constant.driver.StorageConstant;
import io.github.pnoker.common.constant.driver.StrategyConstant;
import io.github.pnoker.common.entity.bo.PointValueBO;
import io.github.pnoker.common.entity.builder.PointValueBuilder;
import io.github.pnoker.common.entity.common.Pages;
import io.github.pnoker.common.entity.model.MgPointValueDO;
import io.github.pnoker.common.entity.query.PointValueQuery;
import io.github.pnoker.common.strategy.RepositoryStrategyFactory;
import io.github.pnoker.common.utils.FieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class MongoRepositoryServiceImpl implements RepositoryService, InitializingBean {

    @Resource
    private PointValueBuilder pointValueBuilder;

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public String getRepositoryName() {
        return StrategyConstant.Storage.MONGO;
    }

    @Override
    public void savePointValue(PointValueBO entityBO) {
        if (!ObjectUtil.isAllNotEmpty(entityBO.getDeviceId(), entityBO.getPointId())) {
            return;
        }

        final String collection = StorageConstant.POINT_VALUE_PREFIX + entityBO.getDeviceId();
        ensurePointValueIndex(collection);
        MgPointValueDO entityDO = pointValueBuilder.buildMgDOByBO(entityBO);
        mongoTemplate.insert(entityDO, collection);
    }

    @Override
    public void savePointValue(Long deviceId, List<PointValueBO> entityBOS) {
        if (ObjectUtil.isEmpty(deviceId)) {
            return;
        }

        final String collection = StorageConstant.POINT_VALUE_PREFIX + deviceId;
        ensurePointValueIndex(collection);
        final List<MgPointValueDO> entityDOS = entityBOS.stream()
                .filter(entityBO -> ObjectUtil.isNotEmpty(entityBO.getPointId()))
                .map(entityBO -> pointValueBuilder.buildMgDOByBO(entityBO))
                .collect(Collectors.toList());
        mongoTemplate.insert(entityDOS, collection);
    }

    @Override
    public List<String> selectHistoryPointValue(Long deviceId, Long pointId, int count) {
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        criteria.and(FieldUtil.getField(PointValueBO::getDeviceId)).is(deviceId).and(FieldUtil.getField(PointValueBO::getPointId)).is(pointId);
        query.fields().include(FieldUtil.getField(PointValueBO::getValue)).exclude(FieldUtil.getField(PointValueBO::getId));
        query.limit(count).with(Sort.by(Sort.Direction.DESC, FieldUtil.getField(PointValueBO::getCreateTime)));

        List<PointValueBO> pointValueBOS = mongoTemplate.find(query, PointValueBO.class, StorageConstant.POINT_VALUE_PREFIX + deviceId);
        return pointValueBOS.stream().map(PointValueBO::getValue).collect(Collectors.toList());
    }

    @Override
    public List<PointValueBO> selectLatestPointValue(Long deviceId, List<Long> pointIds) {
        if (CollUtil.isEmpty(pointIds)) {
            return Collections.emptyList();
        }

        return pointIds.stream().map(pointId -> {
            Criteria criteria = new Criteria();
            Query query = new Query(criteria);
            criteria.and(FieldUtil.getField(PointValueBO::getPointId)).is(pointId);
            query.with(Sort.by(Sort.Direction.DESC, FieldUtil.getField(PointValueBO::getCreateTime)));
            return mongoTemplate.findOne(query, PointValueBO.class, StorageConstant.POINT_VALUE_PREFIX + deviceId);
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public Page<PointValueBO> selectPagePointValue(PointValueQuery entityQuery) {
        if (ObjectUtil.isEmpty(entityQuery.getPage())) {
            entityQuery.setPage(new Pages());
        }

        Page<PointValueBO> entityPageBO = new Page<>();

        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        if (ObjectUtil.isNotEmpty(entityQuery.getDeviceId()))
            criteria.and(FieldUtil.getField(PointValueBO::getDeviceId)).is(entityQuery.getDeviceId());
        if (ObjectUtil.isNotEmpty(entityQuery.getPointId()))
            criteria.and(FieldUtil.getField(PointValueBO::getPointId)).is(entityQuery.getPointId());

        Pages pages = entityQuery.getPage();
        if (pages.getStartTime() > 0 && pages.getEndTime() > 0 && pages.getStartTime() <= pages.getEndTime()) {
            criteria.and(FieldUtil.getField(PointValueBO::getCreateTime)).gte(new Date(pages.getStartTime())).lte(new Date(pages.getEndTime()));
        }

        final String collection = ObjectUtil.isNotEmpty(entityQuery.getDeviceId()) ? StorageConstant.POINT_VALUE_PREFIX + entityQuery.getDeviceId() : PrefixConstant.POINT + SuffixConstant.VALUE;
        long count = mongoTemplate.count(query, collection);
        query.limit((int) pages.getSize()).skip(pages.getSize() * (pages.getCurrent() - 1));
        query.with(Sort.by(Sort.Direction.DESC, FieldUtil.getField(PointValueBO::getCreateTime)));
        List<MgPointValueDO> pointValueDOS = mongoTemplate.find(query, MgPointValueDO.class, collection);
        List<PointValueBO> pointValueBOS = pointValueBuilder.buildBOListByDOList(pointValueDOS);
        entityPageBO.setCurrent(pages.getCurrent()).setSize(pages.getSize()).setTotal(count).setRecords(pointValueBOS);
        return entityPageBO;
    }

    @Override
    public void afterPropertiesSet() {
        RepositoryStrategyFactory.put(StrategyConstant.Storage.MONGO, this);
    }

    /**
     * Ensure device point and time index
     *
     * @param collection Collection Name
     */
    private void ensurePointValueIndex(String collection) {
        // ensure point index
        Index pointIndex = new Index();
        pointIndex.background()
                .on("pointId", Sort.Direction.DESC)
                .named("IX_point_id");
        mongoTemplate.indexOps(collection).ensureIndex(pointIndex);

        // ensure time index
        Index timeIndex = new Index();
        timeIndex.background()
                .on("createTime", Sort.Direction.DESC)
                .named("IX_create_time");
        mongoTemplate.indexOps(collection).ensureIndex(timeIndex);
    }

}