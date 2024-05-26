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

package io.github.pnoker.common.prometheus.dashboard.rabbitmq.service.impl;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pnoker.common.prometheus.dashboard.rabbitmq.entity.vo.RabbitMQDataVo;
import io.github.pnoker.common.prometheus.dashboard.rabbitmq.service.RabbitMQMessageService;
import io.github.pnoker.common.prometheus.service.PrometheusService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * RabbitMQMessage Service Impl
 * </p>
 *
 * @author wangshuai
 * @since 2024.3.26
 */
@Service
public class RabbitMQMessageServiceImpl implements RabbitMQMessageService {
    @Resource
    private PrometheusService prometheusService;

    @Override
    public RabbitMQDataVo queryMQInMess(String cluster) {
        try {
            // 构建原始 PromQL 查询字符串
            String promQLQuery = "sum(rate(rabbitmq_global_messages_received_total[60s]) * on(instance) group_left(rabbitmq_cluster) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''})";
            MapBuilder<String, String> builder = MapUtil.builder();
            builder.put("query", promQLQuery);
            String jsonResponse = prometheusService.queryRange(builder.build());

            // 解析 JSON 响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultNode = rootNode.path("data").path("result").get(0);
            List<Double> values = new ArrayList<>();
            List<Long> times = new ArrayList<>();
            for (int i = 0; i < 61; i++) {
                long time = resultNode.path("values").get(i).get(0).asLong();
                times.add(time);
                Double ivalue = resultNode.path("values").get(i).get(1).asDouble();
                values.add(ivalue);
            }
            RabbitMQDataVo rabbitMQDataVo = new RabbitMQDataVo();
            rabbitMQDataVo.setTimes(times);
            rabbitMQDataVo.setValues(values);
            return rabbitMQDataVo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RabbitMQDataVo queryMQReMess(String cluster) {
        try {
            // 构建原始 PromQL 查询字符串
            String promQLQuery = "sum(rabbitmq_queue_messages_ready * on(instance) group_left(rabbitmq_cluster) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''})";
            MapBuilder<String, String> builder = MapUtil.builder();
            builder.put("query", promQLQuery);
            String jsonResponse = prometheusService.queryRange(builder.build());

            // 解析 JSON 响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultNode = rootNode.path("data").path("result").get(0);
            List<Double> values = new ArrayList<>();
            List<Long> times = new ArrayList<>();
            for (int i = 0; i < 61; i++) {
                long time = resultNode.path("values").get(i).get(0).asLong();
                times.add(time);
                Double ivalue = resultNode.path("values").get(i).get(1).asDouble();
                values.add(ivalue);
            }
            RabbitMQDataVo rabbitMQDataVo = new RabbitMQDataVo();
            rabbitMQDataVo.setTimes(times);
            rabbitMQDataVo.setValues(values);
            return rabbitMQDataVo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RabbitMQDataVo queryMQUnackMess(String cluster) {
        try {
            // 构建原始 PromQL 查询字符串
            String promQLQuery = "sum(rabbitmq_queue_messages_unacked * on(instance) group_left(rabbitmq_cluster) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''})";
            MapBuilder<String, String> builder = MapUtil.builder();
            builder.put("query", promQLQuery);
            String jsonResponse = prometheusService.queryRange(builder.build());

            // 解析 JSON 响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultNode = rootNode.path("data").path("result").get(0);
            List<Double> values = new ArrayList<>();
            List<Long> times = new ArrayList<>();
            for (int i = 0; i < 61; i++) {
                long time = resultNode.path("values").get(i).get(0).asLong();
                times.add(time);
                Double ivalue = resultNode.path("values").get(i).get(1).asDouble();
                values.add(ivalue);
            }
            RabbitMQDataVo rabbitMQDataVo = new RabbitMQDataVo();
            rabbitMQDataVo.setTimes(times);
            rabbitMQDataVo.setValues(values);
            return rabbitMQDataVo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RabbitMQDataVo queryMQOutMess(String cluster) {
        try {
            // 构建原始 PromQL 查询字符串
            String promQLQuery1 = "sum(rate(rabbitmq_global_messages_redelivered_total[60s]) * on(instance) group_left(rabbitmq_cluster) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''})+";
            String promQLQuery2 = "sum(rate(rabbitmq_global_messages_delivered_consume_auto_ack_total[60s]) * on(instance) group_left(rabbitmq_cluster) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''})+";
            String promQLQuery3 = "sum(rate(rabbitmq_global_messages_delivered_consume_manual_ack_total[60s]) * on(instance) group_left(rabbitmq_cluster) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''})+";
            String promQLQuery4 = "sum(rate(rabbitmq_global_messages_delivered_get_auto_ack_total[60s]) * on(instance) group_left(rabbitmq_cluster) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''})+";
            String promQLQuery5 = "sum(rate(rabbitmq_global_messages_delivered_get_manual_ack_total[60s]) * on(instance) group_left(rabbitmq_cluster) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''})";
            String promQLQuery = promQLQuery1 + promQLQuery2 + promQLQuery3 + promQLQuery4 + promQLQuery5;
            MapBuilder<String, String> builder = MapUtil.builder();
            builder.put("query", promQLQuery);
            String jsonResponse = prometheusService.queryRange(builder.build());

            // 解析 JSON 响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultNode = rootNode.path("data").path("result").get(0);
            List<Double> values = new ArrayList<>();
            List<Long> times = new ArrayList<>();
            for (int i = 0; i < 61; i++) {
                long time = resultNode.path("values").get(i).get(0).asLong();
                times.add(time);
                Double ivalue = resultNode.path("values").get(i).get(1).asDouble();
                values.add(ivalue);
            }
            RabbitMQDataVo rabbitMQDataVo = new RabbitMQDataVo();
            rabbitMQDataVo.setTimes(times);
            rabbitMQDataVo.setValues(values);
            return rabbitMQDataVo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}