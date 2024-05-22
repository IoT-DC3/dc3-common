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

package io.github.pnoker.common.prometheus.dashboard.rabbitmq.controller;

import io.github.pnoker.common.constant.service.DataConstant;
import io.github.pnoker.common.entity.R;
import io.github.pnoker.common.prometheus.dashboard.rabbitmq.entity.vo.RabbitMQClusterVo;
import io.github.pnoker.common.prometheus.dashboard.rabbitmq.service.RabbitMQClusterService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * RabbitMQ集群 Controller
 *
 * @author wangshuai
 * @since 2024.3.26
 */
@Slf4j
@RestController
@RequestMapping(DataConstant.RABBITMQ_CLUSTER_URL_PREFIX)
public class RabbitMQClusterController {

    @Resource
    private RabbitMQClusterService rabbitMQClusterService;

    @GetMapping("/clusters")
    public Flux<R<List<RabbitMQClusterVo>>> queryCluster() {
        try {
            List<RabbitMQClusterVo> rabbbit = rabbitMQClusterService.queryCluster();
            if (rabbbit != null) {
                return Flux.just(R.ok(rabbbit));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Flux.just(R.fail(e.getMessage()));
        }
        return Flux.just(R.fail());
    }
}
