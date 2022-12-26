/*
 * Copyright 2016-present Pnoker All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.pnoker.common.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.pnoker.common.constant.common.TimeConstant;
import io.github.pnoker.common.valid.Update;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础 domain 实体类
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID,自动生成
     */
    @TableId(type = IdType.ASSIGN_ID)
    @NotNull(message = "Id can't be empty", groups = {Update.class})
    private String id;

    /**
     * 描述
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = TimeConstant.COMPLETE_DATE_FORMAT, timezone = TimeConstant.TIMEZONE)
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = TimeConstant.COMPLETE_DATE_FORMAT, timezone = TimeConstant.TIMEZONE)
    private Date updateTime;

    /**
     * 逻辑删标识
     * <ol>
     * <li>1 (true):已删除</li>
     * <li>0 (false):默认,未删除</li>
     * </ol>
     */
    @TableLogic
    @TableField(select = false)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer deleted;
}
