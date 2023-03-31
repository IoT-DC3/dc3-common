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

package io.github.pnoker.common.entity.common;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础查询类，其中包括分页以及排序
 *
 * @author pnoker
 * @since 2022.1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pages implements Serializable {
    private static final long serialVersionUID = 1L;

    private long current = 1;
    private long size = 20;
    private long startTime;
    private long endTime;
    private List<OrderItem> orders = new ArrayList<>(4);

    /**
     * Pages convert to Page
     *
     * @param <T> T
     * @return Page
     */
    public <T> Page<T> convert() {
        Page<T> page = buildPageByPages(this);

        boolean createTimeOrder = false;
        for (OrderItem order : page.orders()) {
            if (CharSequenceUtil.isNotEmpty(order.getColumn()) && order.getColumn().equals("operate_time")) {
                createTimeOrder = true;
            }
        }

        // 默认按 operate_time 倒序
        if (!createTimeOrder) {
            page.orders().add(OrderItem.desc("operate_time"));
        }
        return page;
    }

    /**
     * Pages to Page
     *
     * @param pages Pages
     * @param <T>   T
     * @return Page
     */
    private <T> Page<T> buildPageByPages(Pages pages) {
        Page<T> page = new Page<>();

        page.setCurrent(pages.getCurrent());
        page.setSize(pages.getSize());
        page.setOrders(pages.getOrders());

        return page;
    }

}
