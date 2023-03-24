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

import io.github.pnoker.common.constant.driver.RabbitConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Data
@Configuration
public class TopicConfig {

    @Bean
    TopicExchange registerExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE_REGISTER, true, false);
    }

    @Bean
    TopicExchange eventExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE_EVENT, true, false);
    }

    @Bean
    TopicExchange metadataExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE_METADATA, true, false);
    }

    @Bean
    TopicExchange commandExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE_COMMAND, true, false);
    }

    @Bean
    TopicExchange valueExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE_VALUE, true, false);
    }

    @Bean
    TopicExchange mqttExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE_MQTT, true, false);
    }

}
