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

package io.github.pnoker.common.utils;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.pnoker.common.constant.common.ExceptionConstant;
import io.github.pnoker.common.entity.common.Pages;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 字段名称相关工具类
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
public class PageUtil {

    private PageUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * Pages to Page
     *
     * @param pages Pages
     * @param <T>   T
     * @return Page
     */
    public static <T> Page<T> page(Pages pages) {
        Page<T> page = new Page<>();
        if (ObjectUtil.isNull(pages)) {
            pages = new Pages();
        }

        page.setCurrent(pages.getCurrent());
        page.setSize(pages.getSize());
        page.setOrders(pages.getOrders());

        // 添加默认的 operate_time 排序：倒序
        Optional<OrderItem> operateTime = page.orders().stream()
                .filter(order -> CharSequenceUtil.isNotEmpty(order.getColumn()) && order.getColumn().equals("operate_time"))
                .findFirst();
        if (!operateTime.isPresent()) {
            page.orders().add(OrderItem.desc("operate_time"));
        }

        return page;
    }
}
