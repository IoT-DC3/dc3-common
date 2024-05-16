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
import io.github.pnoker.common.prometheus.dashboard.rabbitmq.entity.vo.RabbitMQNodeVo;
import io.github.pnoker.common.prometheus.dashboard.rabbitmq.service.RabbitMQNodeService;
import io.github.pnoker.common.prometheus.service.PrometheusService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * RabbitMQNode Service Impl
 * </p>
 *
 * @author wangshuai
 * @since 2024.3.26
 */
@Service
public class RabbitMQNodeServiceImpl implements RabbitMQNodeService {
    @Resource
    private PrometheusService prometheusService;

    @Override
    public RabbitMQDataVo queryNode(String cluster) {
        try {
            // 构建原始 PromQL 查询字符串
            String promQLQuery = "sum(rabbitmq_build_info * on(instance) group_left(rabbitmq_cluster) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''})";
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
    public List<RabbitMQNodeVo> queryNodeTable(String cluster) {
        try {
            // 构建原始 PromQL 查询字符串
            String promQLQuery = "rabbitmq_build_info * on(instance) group_left(rabbitmq_cluster, rabbitmq_node) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''}";
            MapBuilder<String, String> builder = MapUtil.builder();
            builder.put("query", promQLQuery);
            String jsonResponse = prometheusService.query(builder.build());
            // 解析 JSON 响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            //获取节点个数
            int n = rootNode.path("data").path("result").size();
            List<RabbitMQNodeVo> rabbitMQNodeVoList = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                JsonNode metricNode = rootNode.path("data").path("result").get(i).path("metric");
                JsonNode valueNode = rootNode.path("data").path("result").get(i).path("value");
                RabbitMQNodeVo rabbitMQNodeVo = new RabbitMQNodeVo();
                RabbitMQNodeVo.Metric metric = new RabbitMQNodeVo.Metric();
                RabbitMQNodeVo.ValueItem valueItem = new RabbitMQNodeVo.ValueItem();
                //给metric赋值
                metric.setErlangVersion(metricNode.path("erlang_version").asText());
                String instance = null;
                if(metricNode.path("instance").asText().contains("rabbit")){
                  instance =  metricNode.path("instance").asText().replace("rabbit", "");
                }
                metric.setInstance(instance);
                String job = null;
                if(metricNode.path("job").asText().contains("rabbit")){
                    job =  metricNode.path("job").asText().replace("rabbit", "");
                }
                metric.setJob(job);
                String rabbitmq_cluster = null;
                if(metricNode.path("rabbitmq_cluster").asText().contains("rabbit")){
                    rabbitmq_cluster =  metricNode.path("rabbitmq_cluster").asText().replace("rabbit", "");
                }
                metric.setRabbitmqCluster(rabbitmq_cluster);
                String rabbitmq_node = null;
                if(metricNode.path("rabbitmq_node").asText().contains("rabbit")){
                    rabbitmq_node =  metricNode.path("rabbitmq_node").asText().replace("rabbit", "");
                }
                metric.setRabbitmqNode(rabbitmq_node);
                metric.setRabbitmqVersion(metricNode.path("rabbitmq_version").asText());
                rabbitMQNodeVo.setMetric(metric);
                //给value赋值
                valueItem.setTValue(prometheusService.UnTimeUnix(valueNode.get(0).asDouble()));
                valueItem.setSValue(valueNode.get(1).asText());
                rabbitMQNodeVo.setValue(valueItem);
                rabbitMQNodeVoList.add(rabbitMQNodeVo);
            }
            return rabbitMQNodeVoList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}