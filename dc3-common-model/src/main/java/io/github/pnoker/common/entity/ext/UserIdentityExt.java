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

package io.github.pnoker.common.entity.ext;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * User Ext
 * <p>
 * 用户身份相关拓展信息
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户身份相关拓展")
public class UserIdentityExt extends BaseExt {

    /**
     * 拓展内容
     * <p>
     * 拓展内容可以通过 Type 和 Version 进行区分
     */
    @Schema(description = "类型")
    private Content content;

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "内容")
    public static class Content {
        /**
         * 真实姓名
         */
        @Schema(description = "真实姓名")
        private String realName;

        /**
         * 性别
         */
        @Schema(description = "性别")
        private String gender;

        /**
         * 国籍
         */
        @Schema(description = "国籍")
        private String nationality;

        /**
         * 身份证号码
         */
        @Schema(description = "身份证号码")
        private String idNumber;
    }
}
