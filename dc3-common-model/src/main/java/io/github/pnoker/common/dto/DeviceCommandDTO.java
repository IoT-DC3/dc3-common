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

package io.github.pnoker.common.dto;

import io.github.pnoker.common.enums.DeviceCommandTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备指令
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCommandDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 指令类型
     */
    private DeviceCommandTypeEnum type;

    /**
     * 指令内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    public DeviceCommandDTO(DeviceCommandTypeEnum type, String content) {
        this.type = type;
        this.content = content;
        this.createTime = new Date();
    }

    /**
     * 设备读指令
     *
     * @author pnoker
     * @since 2022.1.0
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceRead implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 设备ID
         */
        private String deviceId;

        /**
         * 位号ID
         */
        private String pointId;


        /**
         * 创建时间
         */
        private Date createTime;

        public DeviceRead(String deviceId, String pointId) {
            this.deviceId = deviceId;
            this.pointId = pointId;
            this.createTime = new Date();
        }
    }

    /**
     * 设备写指令
     *
     * @author pnoker
     * @since 2022.1.0
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceWrite implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 设备ID
         */
        private String deviceId;

        /**
         * 位号ID
         */
        private String pointId;

        /**
         * 待写入的值
         */
        private String value;

        /**
         * 创建时间
         */
        private Date createTime;

        public DeviceWrite(String deviceId, String pointId, String value) {
            this.deviceId = deviceId;
            this.pointId = pointId;
            this.value = value;
            this.createTime = new Date();
        }
    }
}
