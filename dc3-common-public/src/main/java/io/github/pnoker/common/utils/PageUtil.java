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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.pnoker.common.constant.common.DefaultConstant;
import io.github.pnoker.common.constant.common.ExceptionConstant;
import io.github.pnoker.common.entity.common.Pages;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页相关工具类
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
     * 自定义 Pages 转 MyBatis Plus Page
     *
     * @param pages {@link Pages}
     * @param <T>   T
     * @return {@link Page}
     */
    public static <T> Page<T> page(Pages pages) {
        Page<T> page = new Page<>();
        if (ObjectUtil.isNull(pages)) {
            pages = new Pages();
        }
        if (pages.getCurrent() < 1) {
            pages.setCurrent(1);
        }
        if (pages.getSize() > DefaultConstant.DEFAULT_MAX_PAGE_SIZE) {
            pages.setSize(DefaultConstant.DEFAULT_MAX_PAGE_SIZE);
        }
        List<OrderItem> orders = pages.getOrders();
        if (CollUtil.isEmpty(orders)) {
            orders = new ArrayList<>(2);
        }
        orders = orders.stream().filter(order -> ObjectUtil.isNotNull(order) && CharSequenceUtil.isNotEmpty(order.getColumn())).toList();
        boolean match = orders.stream().anyMatch(order -> ObjectUtil.isNotNull(order) && "create_time".equals(order.getColumn()));
        if (!match) {
            orders.add(OrderItem.desc("create_time"));
        }

        page.setCurrent(pages.getCurrent());
        page.setSize(pages.getSize());
        page.setOrders(orders);
        return page;
    }
}
