/*
 * Copyright 2016-present the original author or authors.
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

package io.github.pnoker.common.config;

import io.github.pnoker.common.constant.common.EnvironmentConstant;
import io.github.pnoker.common.constant.driver.RabbitConstant;
import io.github.pnoker.common.utils.EnvironmentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Environment Config
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
public class RabbitmqEnvironmentConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String env = environment.getProperty(EnvironmentConstant.SPRING_ENV, String.class);
        String group = environment.getProperty(EnvironmentConstant.SPRING_GROUP, String.class);

        String tag = EnvironmentUtil.getTag(env, group);

        // Sync
        RabbitConstant.TOPIC_EXCHANGE_SYNC = tag + RabbitConstant.TOPIC_EXCHANGE_SYNC;
        RabbitConstant.QUEUE_SYNC_UP = tag + RabbitConstant.QUEUE_SYNC_UP;
        RabbitConstant.QUEUE_SYNC_DOWN_PREFIX = tag + RabbitConstant.QUEUE_SYNC_DOWN_PREFIX;

        // Event
        RabbitConstant.TOPIC_EXCHANGE_EVENT = tag + RabbitConstant.TOPIC_EXCHANGE_EVENT;
        RabbitConstant.QUEUE_DRIVER_EVENT = tag + RabbitConstant.QUEUE_DRIVER_EVENT;
        RabbitConstant.QUEUE_DEVICE_EVENT = tag + RabbitConstant.QUEUE_DEVICE_EVENT;

        // Metadata
        RabbitConstant.TOPIC_EXCHANGE_METADATA = tag + RabbitConstant.TOPIC_EXCHANGE_METADATA;
        RabbitConstant.QUEUE_DRIVER_METADATA_PREFIX = tag + RabbitConstant.QUEUE_DRIVER_METADATA_PREFIX;

        // Command
        RabbitConstant.TOPIC_EXCHANGE_COMMAND = tag + RabbitConstant.TOPIC_EXCHANGE_COMMAND;
        RabbitConstant.QUEUE_DRIVER_COMMAND_PREFIX = tag + RabbitConstant.QUEUE_DRIVER_COMMAND_PREFIX;
        RabbitConstant.QUEUE_DEVICE_COMMAND_PREFIX = tag + RabbitConstant.QUEUE_DEVICE_COMMAND_PREFIX;

        // Point Value
        RabbitConstant.TOPIC_EXCHANGE_VALUE = tag + RabbitConstant.TOPIC_EXCHANGE_VALUE;
        RabbitConstant.QUEUE_POINT_VALUE = tag + RabbitConstant.QUEUE_POINT_VALUE;
    }

}
