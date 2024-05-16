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
import io.github.pnoker.common.prometheus.dashboard.rabbitmq.entity.vo.RabbitMQDataVo;
import io.github.pnoker.common.prometheus.dashboard.rabbitmq.service.RabbitMQMessageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RabbitMQ消息 Controller
 *
 * @author wangshuai
 * @since 2024.3.26
 */
@Slf4j
@RestController
@RequestMapping(DataConstant.RABBITMQ_MESSAGE_URL_PREFIX)
public class RabbitMQMessageController {
    @Resource
    private RabbitMQMessageService rabbitMQMessageService;

    @GetMapping("/message_in")
    public R<RabbitMQDataVo> queryInMess(@RequestParam String cluster) {
        try {
            RabbitMQDataVo rabbbit = rabbitMQMessageService.queryMQInMess(cluster);
            if (!rabbbit.getTimes().isEmpty() && !rabbbit.getValues().isEmpty()) {
                return R.ok(rabbbit);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    @GetMapping("/message_ready")
    public R<RabbitMQDataVo> queryReMess(@RequestParam String cluster) {
        try {
            RabbitMQDataVo rabbbit = rabbitMQMessageService.queryMQReMess(cluster);
            if (!rabbbit.getTimes().isEmpty() && !rabbbit.getValues().isEmpty()) {
                return R.ok(rabbbit);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    @GetMapping("/message_unack")
    public R<RabbitMQDataVo> queryUnackMess(@RequestParam String cluster) {
        try {
            RabbitMQDataVo rabbbit = rabbitMQMessageService.queryMQUnackMess(cluster);
            if (!rabbbit.getTimes().isEmpty() && !rabbbit.getValues().isEmpty()) {
                return R.ok(rabbbit);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    @GetMapping("/message_out")
    public R<RabbitMQDataVo> queryOutMess(@RequestParam String cluster) {
        try {
            RabbitMQDataVo rabbbit = rabbitMQMessageService.queryMQOutMess(cluster);
            if (!rabbbit.getTimes().isEmpty() && !rabbbit.getValues().isEmpty()) {
                return R.ok(rabbbit);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

}
