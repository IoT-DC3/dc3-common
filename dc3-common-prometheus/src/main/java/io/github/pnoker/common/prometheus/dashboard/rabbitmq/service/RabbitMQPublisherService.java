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

package io.github.pnoker.common.prometheus.dashboard.rabbitmq.service;

import io.github.pnoker.common.prometheus.dashboard.rabbitmq.entity.vo.RabbitMQDataVo;

/**
 * RabbitMQPublisher Interface
 *
 * @author wangshuai
 * @since 2024.3.26
 */
public interface RabbitMQPublisherService {
    RabbitMQDataVo queryPub(String cluster);
}
