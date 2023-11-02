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

package io.github.pnoker.common.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 基础 Service 类接口
 *
 * @author pnoker
 * @since 2022.1.0
 */
public interface Service<DO, Q> {
    /**
     * 新增
     *
     * @param entityDO Entity of DO
     */
    void add(DO entityDO);

    /**
     * 删除
     *
     * @param id ID
     */
    void delete(Long id);

    /**
     * 更新
     *
     * @param entityDO Entity of DO
     */
    void update(DO entityDO);

    /**
     * 通过 ID 查询
     *
     * @param id ID
     * @return Entity of DO
     */
    DO selectById(Long id);

    /**
     * 获取带分页、排序
     *
     * @param queryDTO Query DTO
     * @return Entity of DO Page
     */
    Page<DO> list(Q queryDTO);
}
