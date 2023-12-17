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

package io.github.pnoker.common.entity.dto;

import io.github.pnoker.common.constant.enums.DeviceEventTypeEnum;
import io.github.pnoker.common.constant.enums.DriverStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备事件
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class DeviceEventDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 事件类型
     */
    private DeviceEventTypeEnum type;

    /**
     * 事件内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 设备事件
     *
     * @author pnoker
     * @since 2022.1.0
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceStatus implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 驱动服务名称
         */
        private String serviceName;

        /**
         * 驱动状态
         */
        private DriverStatusEnum status;

        /**
         * 创建时间
         */
        private LocalDateTime createTime;

        public DeviceStatus(String serviceName, DriverStatusEnum status) {
            this.serviceName = serviceName;
            this.status = status;
            this.createTime = LocalDateTime.now();
        }
    }
}
