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

package io.github.pnoker.common.entity.property;

import lombok.Getter;
import lombok.Setter;

/**
 * 通用线程池属性
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Setter
@Getter
public class ThreadProperty {

    /**
     * 线程名称前缀
     */
    private String prefix;

    /**
     * 线程池核心线程数量
     */
    private int corePoolSize;

    /**
     * 线程池线程最大数量
     */
    private int maximumPoolSize;

    /**
     * 空闲线程等待时间
     */
    private int keepAliveTime;
}
