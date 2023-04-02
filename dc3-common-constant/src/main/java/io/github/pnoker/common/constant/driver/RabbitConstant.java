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

package io.github.pnoker.common.constant.driver;

import io.github.pnoker.common.constant.common.ExceptionConstant;

/**
 * 消息 相关常量
 *
 * @author pnoker
 * @since 2022.1.0
 */
public class RabbitConstant {

    private RabbitConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    // Arguments
    public static final String MESSAGE_TTL = "x-message-ttl";
    public static final String AUTO_DELETE = "x-auto-delete";

    // Sync
    public static String TOPIC_EXCHANGE_SYNC = "dc3.e.sync";
    public static final String ROUTING_SYNC_UP_PREFIX = "dc3.r.sync.up.";
    public static String QUEUE_SYNC_UP = "dc3.q.sync.up";
    public static final String ROUTING_SYNC_DOWN_PREFIX = "dc3.r.sync.down.";
    public static String QUEUE_SYNC_DOWN_PREFIX = "dc3.q.sync.down.";

    // Event
    public static String TOPIC_EXCHANGE_EVENT = "dc3.e.event";
    public static final String ROUTING_DRIVER_EVENT_PREFIX = "dc3.r.event.driver.";
    public static String QUEUE_DRIVER_EVENT = "dc3.q.event.driver";
    public static final String ROUTING_DEVICE_EVENT_PREFIX = "dc3.r.event.device.";
    public static String QUEUE_DEVICE_EVENT = "dc3.q.event.device";

    // Metadata
    public static String TOPIC_EXCHANGE_METADATA = "dc3.e.metadata";
    public static final String ROUTING_DRIVER_METADATA_PREFIX = "dc3.r.metadata.driver.";
    public static String QUEUE_DRIVER_METADATA_PREFIX = "dc3.q.metadata.driver.";

    // Command
    public static String TOPIC_EXCHANGE_COMMAND = "dc3.e.command";
    public static final String ROUTING_DRIVER_COMMAND_PREFIX = "dc3.r.command.driver.";
    public static String QUEUE_DRIVER_COMMAND_PREFIX = "dc3.q.command.driver.";
    public static final String ROUTING_DEVICE_COMMAND_PREFIX = "dc3.r.command.device.";
    public static String QUEUE_DEVICE_COMMAND_PREFIX = "dc3.q.command.device.";

    // Value
    public static String TOPIC_EXCHANGE_VALUE = "dc3.e.value";
    public static final String ROUTING_POINT_VALUE_PREFIX = "dc3.r.value.point.";
    public static String QUEUE_POINT_VALUE = "dc3.q.value.point";

    // Mqtt
    public static String TOPIC_EXCHANGE_MQTT = "dc3.e.mqtt";
    public static final String ROUTING_MQTT_PREFIX = "dc3.r.mqtt.";
    public static String QUEUE_MQTT = "dc3.q.mqtt";

}
