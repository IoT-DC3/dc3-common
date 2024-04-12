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

package io.github.pnoker.common.influx.repository;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import io.github.pnoker.common.constant.driver.StrategyConstant;
import io.github.pnoker.common.entity.bo.PointValueBO;
import io.github.pnoker.common.entity.common.Pages;
import io.github.pnoker.common.entity.query.PointValueQuery;
import io.github.pnoker.common.influx.entity.builder.InfluxPointValueBuilder;
import io.github.pnoker.common.influx.entity.model.InfluxMapperBO;
import io.github.pnoker.common.influx.entity.model.InfluxMapperDO;
import io.github.pnoker.common.influx.entity.model.InfluxPointValueDO;
import io.github.pnoker.common.repository.RepositoryService;
import io.github.pnoker.common.strategy.RepositoryStrategyFactory;
import io.github.pnoker.common.utils.FieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service("influxRepositoryService")
public class InfluxRepositoryServiceImpl implements RepositoryService, InitializingBean {

    @Resource
    private InfluxPointValueBuilder influxPointValueBuilder;
    @Resource
    private InfluxDBClient influxDBClient;

    @Override
    public String getRepositoryName() {
        return StrategyConstant.Storage.INFLUXDB;
    }

    @Override
    public void savePointValue(PointValueBO entityBO) {
        if (!ObjectUtil.isAllNotEmpty(entityBO.getDeviceId(), entityBO.getPointId())) {
            return;
        }
        WriteApiBlocking writeApiBlocking = influxDBClient.getWriteApiBlocking();
        InfluxPointValueDO influxPointValueDO = influxPointValueBuilder.buildMgDOByBO(entityBO);
        Point point = Point.measurement("dc3")
                .addTag("deviceId", influxPointValueDO.getDeviceId().toString())
                .addTag("pointId", influxPointValueDO.getPointId().toString())
                .addTag("originTime", influxPointValueDO.getOriginTime().toString())
                .addField("value", Long.valueOf(influxPointValueDO.getValue()))
                .addField("rawValue", Long.valueOf(influxPointValueDO.getRawValue()))
                .time(Instant.now(), WritePrecision.MS);
        writeApiBlocking.writePoint(point);

    }

    @Override
    public void savePointValue(Long deviceId, List<PointValueBO> entityBOS) {
        if (ObjectUtil.isEmpty(deviceId)) {
            return;
        }
        WriteApiBlocking writeApiBlocking = influxDBClient.getWriteApiBlocking();
        List<InfluxPointValueDO> influxPointValueDOS = influxPointValueBuilder.buildMgDOListByBOList(entityBOS);
        for (InfluxPointValueDO influxPointValueDO : influxPointValueDOS) {
            Point point = Point.measurement("dc3")
                    .addTag("deviceId", influxPointValueDO.getDeviceId().toString())
                    .addTag("pointId", influxPointValueDO.getPointId().toString())
                    .addTag("originTime",influxPointValueDO.getOriginTime().toString() )
                    .addField("value", Long.valueOf(influxPointValueDO.getValue()))
                    .addField("rawValue", Long.valueOf(influxPointValueDO.getRawValue()))
                    .time(Instant.now(), WritePrecision.MS);
            writeApiBlocking.writePoint(point);
        }
    }

    @Override
    public List<String> selectHistoryPointValue(Long deviceId, Long pointId, int count) {
        return null;
    }

    @Override
    public List<PointValueBO> selectLatestPointValue(Long deviceId, List<Long> pointIds) {
        return null;
    }

    @Override
    public Page<PointValueBO> selectPagePointValue(PointValueQuery entityQuery) {
        if (ObjectUtil.isEmpty(entityQuery.getPage())) {
            entityQuery.setPage(new Pages());
        }
        QueryApi queryApi = influxDBClient.getQueryApi();
        StringBuilder flux = new StringBuilder();
        flux.append("from(bucket: \"dc3\")"
                + " |> range(start: -100h)" // 查询过去1小时的数据
                + " |> filter(fn: (r) => r._measurement == \"dc3\"");
        Page<PointValueBO> entityPageBO = new Page<>();
        if (ObjectUtil.isNotEmpty(entityQuery.getDeviceId())) {
            flux.append("and r.deviceId ==\"" + entityQuery.getDeviceId() + "\"");
        }
        if (ObjectUtil.isNotEmpty(entityQuery.getPointId())) {
            flux.append("and r.pointId ==\"" + entityQuery.getPointId() + "\"");
        }
        flux.append(")");
        System.out.println(flux.toString());
        List<InfluxMapperDO> query = queryApi.query(flux.toString(), InfluxMapperDO.class);
        List<InfluxMapperBO> influxMapperBOS = convertToBO(query);
        List<InfluxPointValueDO> influxPointValueDOS = convertMapperBoToValueDo(influxMapperBOS);
        Pages pages = entityQuery.getPage();
        List<InfluxPointValueDO> page = getPage(influxPointValueDOS, (int) pages.getSize(), (int) pages.getCurrent());
        List<PointValueBO> pointValueBOS = influxPointValueBuilder.buildBOListByDOList(page);
        Page<PointValueBO> pointValueBOPage = entityPageBO.setCurrent(pages.getCurrent()).setSize(pages.getSize()).setTotal(influxPointValueDOS.size()).setRecords(pointValueBOS);
        return pointValueBOPage;
    }

    @Override
    public void afterPropertiesSet() {
        RepositoryStrategyFactory.put(StrategyConstant.Storage.INFLUXDB, this);
    }

    /**
     * mapper转换DO
     *
     * @param listBo
     * @return
     */
    public static List<InfluxPointValueDO> convertMapperBoToValueDo(List<InfluxMapperBO> listBo) {
        List<InfluxPointValueDO> listDo = new ArrayList<>();

        for (InfluxMapperBO bo : listBo) {
            InfluxPointValueDO entity = new InfluxPointValueDO();
            entity.setDeviceId(Long.parseLong(bo.getDeviceId()));
            entity.setPointId(Long.parseLong(bo.getPointId()));
            entity.setRawValue(bo.getRawValue().toString());
            entity.setValue(bo.getValue().toString());
            entity.setOriginTime(LocalDateTime.parse(bo.getOriginTime())); // Assuming originTime is in ISO-8601 format

            // Convert Instant to LocalDateTime
            Instant instant = bo.getTime();
            LocalDateTime time = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            entity.setCreateTime(time);

            listDo.add(entity);
        }

        return listDo;
    }

    /**
     * 分页
     *
     * @param data
     * @param pageSize
     * @param pageNumber
     * @return
     */
    public static List<InfluxPointValueDO> getPage(List<InfluxPointValueDO> data, int pageSize, int pageNumber) {
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, data.size());
        if (startIndex >= endIndex) {
            return new ArrayList<>();
        }
        return data.subList(startIndex, endIndex);
    }

    /**
     * influx库转为BO类
     *
     * @param dos
     * @return
     */
    public static List<InfluxMapperBO> convertToBO(List<InfluxMapperDO> dos) {
        Map<String, InfluxMapperBO> boMap = new HashMap<>();

        for (InfluxMapperDO dobj : dos) {
            String key = dobj.getTime() + "-" + dobj.getDeviceId() + "-" + dobj.getPointId();

            InfluxMapperBO bo = boMap.get(key);
            if (bo == null) {
                bo = new InfluxMapperBO(dobj.getTime(), dobj.getDeviceId(), dobj.getPointId(), dobj.getOriginTime(), null, null);
                boMap.put(key, bo);
            }

            if ("value".equals(dobj.getField())) {
                bo.setValue(dobj.getValue());
            } else if ("rawValue".equals(dobj.getField())) {
                bo.setRawValue(dobj.getValue());
            }
        }

        return new ArrayList<>(boMap.values());
    }
}