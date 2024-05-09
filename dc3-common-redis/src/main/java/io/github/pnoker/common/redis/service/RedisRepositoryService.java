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

package io.github.pnoker.common.redis.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.constant.common.PrefixConstant;
import io.github.pnoker.common.constant.common.SymbolConstant;
import io.github.pnoker.common.entity.bo.PointValueBO;
import io.github.pnoker.common.redis.entity.builder.RedisPointValueBuilder;
import io.github.pnoker.common.redis.entity.model.RedisPointValueDO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class RedisRepositoryService {

    @Resource
    private RedisPointValueBuilder redisPointValueBuilder;

    @Resource
    private RedisService redisService;

    public void savePointValue(PointValueBO entityBO) {
        if (!ObjectUtil.isAllNotEmpty(entityBO.getDeviceId(), entityBO.getPointId())) {
            return;
        }

        final String prefix = PrefixConstant.REAL_TIME_VALUE_KEY_PREFIX + entityBO.getDeviceId() + SymbolConstant.DOT;
        RedisPointValueDO entityDO = redisPointValueBuilder.buildDOByBO(entityBO);
        redisService.setKey(prefix + entityBO.getPointId(), entityDO);
    }

    public void savePointValue(Long deviceId, List<PointValueBO> entityBOS) {
        if (ObjectUtil.isEmpty(deviceId)) {
            return;
        }

        final String prefix = PrefixConstant.REAL_TIME_VALUE_KEY_PREFIX + deviceId + SymbolConstant.DOT;
        List<RedisPointValueDO> entityDOList = redisPointValueBuilder.buildDOListByBOList(entityBOS);
        Map<String, RedisPointValueDO> entityDOMap = entityDOList.stream()
                .filter(entityBO -> ObjectUtil.isNotEmpty(entityBO.getPointId()))
                .collect(Collectors.toMap(entityBO -> prefix + entityBO.getPointId(), Function.identity()));
        redisService.setKey(entityDOMap);
    }

    public List<PointValueBO> selectLatestPointValue(Long deviceId, List<Long> pointIds) {
        if (CollUtil.isEmpty(pointIds)) {
            return Collections.emptyList();
        }

        String prefix = PrefixConstant.REAL_TIME_VALUE_KEY_PREFIX + deviceId + SymbolConstant.DOT;
        List<String> keys = pointIds.stream().map(pointId -> prefix + pointId).toList();
        List<RedisPointValueDO> entityDOList = redisService.getKey(keys);
        return redisPointValueBuilder.buildBOListByDOList(entityDOList);
    }

}