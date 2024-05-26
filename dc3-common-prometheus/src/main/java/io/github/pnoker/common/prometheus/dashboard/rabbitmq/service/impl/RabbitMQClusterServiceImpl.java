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
import io.github.pnoker.common.prometheus.dashboard.rabbitmq.entity.vo.RabbitMQClusterVo;
import io.github.pnoker.common.prometheus.dashboard.rabbitmq.service.RabbitMQClusterService;
import io.github.pnoker.common.prometheus.service.PrometheusService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * RabbitMQCluster Service Impl
 * </p>
 *
 * @author wangshuai
 * @since 2024.3.26
 */
@Service
public class RabbitMQClusterServiceImpl implements RabbitMQClusterService {

    @Resource
    private PrometheusService prometheusService;

    @Override
    public List<RabbitMQClusterVo> queryCluster() {
        try {
            MapBuilder<String, String> builder = MapUtil.builder();
            builder.put("query", "rabbitmq_identity_info{namespace=''}");
            String jsonResponse = prometheusService.query(builder.build());

            // 解析 JSON 响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            int n = rootNode.path("data").path("result").size();
            List<RabbitMQClusterVo> clusters = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                RabbitMQClusterVo cluster = new RabbitMQClusterVo();
                JsonNode metricNode = rootNode.path("data").path("result").get(i).path("metric");
                String str = metricNode.path("rabbitmq_cluster").asText();
                cluster.setOriginal(str);
                if (str.contains("rabbit")) {
                    cluster.setDisplay(str.replace("rabbit", ""));
                } else {
                    cluster.setDisplay(str);
                }
                clusters.add(cluster);
            }
            return clusters;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
