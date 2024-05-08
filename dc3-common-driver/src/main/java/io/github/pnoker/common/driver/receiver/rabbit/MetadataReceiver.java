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

import cn.hutool.core.util.ObjectUtil;
import com.rabbitmq.client.Channel;
import io.github.pnoker.common.driver.entity.bo.DeviceBO;
import io.github.pnoker.common.driver.entity.bo.PointBO;
import io.github.pnoker.common.driver.metadata.DeviceMetadata;
import io.github.pnoker.common.driver.metadata.PointMetadata;
import io.github.pnoker.common.entity.dto.DriverTransferMetadataDTO;
import io.github.pnoker.common.enums.MetadataCommandTypeEnum;
import io.github.pnoker.common.utils.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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

    @RabbitHandler
    @RabbitListener(queues = "#{driverMetadataQueue.name}")
    public void driverMetadataReceive(Channel channel, Message message, DriverTransferMetadataDTO entityDTO) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            log.info("Receive driver metadata: {}", JsonUtil.toJsonString(entityDTO));
            if (ObjectUtil.isNull(entityDTO)
                    || ObjectUtil.isNull(entityDTO.getType())
                    || ObjectUtil.isNull(entityDTO.getMetadataCommandType())) {
                log.error("Invalid driver metadata: {}", entityDTO);
                return;
            }

            switch (entityDTO.getType()) {
                case DRIVER -> {
                }
                case DEVICE -> {
                    DeviceBO device = JsonUtil.parseObject(entityDTO.getContent(), DeviceBO.class);
                    if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
                        log.info("Upsert device: {}", JsonUtil.toJsonString(device));
                        deviceMetadata.loadCache(device.getId());
                    } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
                        log.info("Delete device: {}", JsonUtil.toJsonString(device));
                        deviceMetadata.removeCache(device.getId());
                    }
                }
                case POINT -> {
                    PointBO point = JsonUtil.parseObject(entityDTO.getContent(), PointBO.class);
                    if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
                        log.info("Upsert point: {}", JsonUtil.toJsonString(point));
                        pointMetadata.loadCache(point.getId());
                    } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
                        log.info("Delete point: {}", JsonUtil.toJsonString(point));
                        pointMetadata.removeCache(point.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
