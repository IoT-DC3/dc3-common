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

package io.github.pnoker.common.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * MongoDB Object ID
     */
    @MongoId
    private ObjectId id;

    /**
     * 驱动服务名称
     */
    private String serviceName;

    /**
     * Driver Event
     * <p>
     * STATUS、ERROR
     */
    private String type;

    private Boolean confirm = false;
    private Object content;

    private Date createTime;
    private Date updateTime;

    public DriverEvent(String serviceName, String type) {
        this.serviceName = serviceName;
        this.type = type;
        this.createTime = new Date();
    }

    public DriverEvent(String serviceName, String type, Object content) {
        this.serviceName = serviceName;
        this.type = type;
        this.content = content;
        this.createTime = new Date();
    }

}
