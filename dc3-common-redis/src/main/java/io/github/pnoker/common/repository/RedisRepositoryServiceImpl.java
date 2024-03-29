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
import io.github.pnoker.common.constant.common.SymbolConstant;
import io.github.pnoker.common.constant.driver.StrategyConstant;
import io.github.pnoker.common.entity.bo.PointValueBO;
import io.github.pnoker.common.entity.query.PointValueQuery;
import io.github.pnoker.common.redis.RedisService;
import io.github.pnoker.common.strategy.RepositoryStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class RedisRepositoryServiceImpl implements RepositoryService, InitializingBean {

    @Resource
    private RedisService redisService;

    @Override
    public String getRepositoryName() {
        return StrategyConstant.Storage.REDIS;
    }

    @Override
    public void savePointValue(PointValueBO entityBO) {
        if (!ObjectUtil.isAllNotEmpty(entityBO.getDeviceId(), entityBO.getPointId())) {
            return;
        }

        final String prefix = PrefixConstant.REAL_TIME_VALUE_KEY_PREFIX + entityBO.getDeviceId() + SymbolConstant.DOT;
        redisService.setKey(prefix + entityBO.getPointId(), entityBO);
    }

    @Override
    public void savePointValue(Long deviceId, List<PointValueBO> entityBOS) {
        if (ObjectUtil.isEmpty(deviceId)) {
            return;
        }

        final String prefix = PrefixConstant.REAL_TIME_VALUE_KEY_PREFIX + deviceId + SymbolConstant.DOT;
        Map<String, PointValueBO> collect = entityBOS.stream()
                .filter(entityBO -> ObjectUtil.isNotEmpty(entityBO.getPointId()))
                .collect(Collectors.toMap(entityBO -> prefix + entityBO.getPointId(), Function.identity()));
        redisService.setKey(collect);
    }

    @Override
    public List<String> selectHistoryPointValue(Long deviceId, Long pointId, int count) {
        // redis 目前仅用于存放实时数据，不提供历史数据查询
        return null;
    }

    @Override
    public List<PointValueBO> selectLatestPointValue(Long deviceId, List<Long> pointIds) {
        if (CollUtil.isEmpty(pointIds)) {
            return Collections.emptyList();
        }

        String prefix = PrefixConstant.REAL_TIME_VALUE_KEY_PREFIX + deviceId + SymbolConstant.DOT;
        List<String> keys = pointIds.stream().map(pointId -> prefix + pointId).collect(Collectors.toList());
        List<PointValueBO> pointValueBOS = redisService.getKey(keys);
        return pointValueBOS.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public Page<PointValueBO> selectPagePointValue(PointValueQuery entityQuery) {
        // redis 目前仅用于存放实时数据，不提供历史数据查询
        return null;
    }

    @Override
    public void afterPropertiesSet() {
        RepositoryStrategyFactory.put(StrategyConstant.Storage.REDIS, this);
    }
}