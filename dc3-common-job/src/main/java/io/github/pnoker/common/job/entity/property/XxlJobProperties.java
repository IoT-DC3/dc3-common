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

package io.github.pnoker.common.job.entity.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {
    private String adminAddresses;
    private String accessToken;
    private String appName;
    private String address;
    private String ip;
    private int executorPort;
    private String logPath;
    private int logRetentionDays;
}
