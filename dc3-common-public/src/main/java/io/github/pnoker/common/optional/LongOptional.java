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

package io.github.pnoker.common.optional;

import io.github.pnoker.common.constant.common.DefaultConstant;

import java.util.function.LongConsumer;

/**
 * 自定义 long Optional
 *
 * @author pnoker
 * @since 2022.1.0
 */
public final class LongOptional {

    private final long value;

    private LongOptional(long value) {
        this.value = value;
    }

    public static LongOptional of(long value) {
        return new LongOptional(value);
    }

    public void ifPresent(LongConsumer consumer) {
        if (value > DefaultConstant.DEFAULT_INT) {
            consumer.accept(value);
        }
    }
}
