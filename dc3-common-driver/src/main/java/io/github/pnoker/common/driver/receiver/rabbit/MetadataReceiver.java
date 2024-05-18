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

package io.github.pnoker.common.driver.receiver.rabbit;

import com.rabbitmq.client.Channel;
import io.github.pnoker.common.driver.entity.bo.DeviceBO;
import io.github.pnoker.common.driver.entity.bo.PointBO;
import io.github.pnoker.common.driver.event.device.DeviceMetadataEvent;
import io.github.pnoker.common.driver.event.device.DeviceMetadataEventPublisher;
import io.github.pnoker.common.driver.event.point.PointMetadataEvent;
import io.github.pnoker.common.driver.event.point.PointMetadataEventPublisher;
import io.github.pnoker.common.driver.metadata.DeviceMetadata;
import io.github.pnoker.common.driver.metadata.PointMetadata;
import io.github.pnoker.common.entity.dto.DriverTransferMetadataDTO;
import io.github.pnoker.common.enums.MetadataOperateTypeEnum;
import io.github.pnoker.common.utils.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 接收驱动元数据
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class MetadataReceiver {

    @Resource
    private DeviceMetadata deviceMetadata;
    @Resource
    PointMetadata pointMetadata;

    @Resource
    private DeviceMetadataEventPublisher deviceMetadataEventPublisher;
    @Resource
    private PointMetadataEventPublisher pointMetadataEventPublisher;

    @RabbitHandler
    @RabbitListener(queues = "#{metadataQueue.name}")
    public void metadataReceive(Channel channel, Message message, DriverTransferMetadataDTO entityDTO) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            log.info("Receive driver metadata: {}", JsonUtil.toJsonString(entityDTO));
            if (Objects.isNull(entityDTO)
                    || Objects.isNull(entityDTO.getType())
                    || Objects.isNull(entityDTO.getMetadataCommandType())) {
                log.error("Invalid driver metadata: {}", entityDTO);
                return;
            }

            switch (entityDTO.getType()) {
                case DRIVER -> {
                }
                case DEVICE -> {
                    DeviceBO device = JsonUtil.parseObject(entityDTO.getContent(), DeviceBO.class);
                    if (MetadataOperateTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataOperateTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
                        log.info("Upsert device: {}", JsonUtil.toJsonString(device));
                        deviceMetadata.loadCache(device.getId());
                    } else if (MetadataOperateTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
                        log.info("Delete device: {}", JsonUtil.toJsonString(device));
                        deviceMetadata.removeCache(device.getId());
                    }

                    // publish device metadata event
                    deviceMetadataEventPublisher.publishEvent(new DeviceMetadataEvent(this, device.getId(), entityDTO.getMetadataCommandType(), device));
                }
                case POINT -> {
                    PointBO point = JsonUtil.parseObject(entityDTO.getContent(), PointBO.class);
                    if (MetadataOperateTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataOperateTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
                        log.info("Upsert point: {}", JsonUtil.toJsonString(point));
                        pointMetadata.loadCache(point.getId());
                    } else if (MetadataOperateTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
                        log.info("Delete point: {}", JsonUtil.toJsonString(point));
                        pointMetadata.removeCache(point.getId());
                    }

                    // publish point metadata event
                    pointMetadataEventPublisher.publishEvent(new PointMetadataEvent(this, point.getId(), entityDTO.getMetadataCommandType(), point));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
