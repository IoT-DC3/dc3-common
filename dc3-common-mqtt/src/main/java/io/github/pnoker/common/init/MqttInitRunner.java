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

package io.github.pnoker.common.init;

import io.github.pnoker.common.mqtt.entity.property.MqttProperties;
import io.github.pnoker.common.mqtt.service.MqttScheduleService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Mqtt Initial
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Component
@ComponentScan(basePackages = {
        "io.github.pnoker.common.mqtt.*"
})
@EnableConfigurationProperties({MqttProperties.class})
public class MqttInitRunner implements ApplicationRunner {

    private final MqttScheduleService mqttScheduleService;

    public MqttInitRunner(MqttScheduleService mqttScheduleService) {
        this.mqttScheduleService = mqttScheduleService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mqttScheduleService.initial();
    }
}
