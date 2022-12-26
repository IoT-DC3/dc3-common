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

package io.github.pnoker.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.pnoker.common.bean.model.BaseModel;
import io.github.pnoker.common.valid.Auth;
import io.github.pnoker.common.valid.Insert;
import io.github.pnoker.common.valid.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * User
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel {

    @NotBlank(message = "Name can't be empty",
            groups = {Insert.class, Auth.class})
    @Pattern(regexp = "^[a-zA-Z]\\w{2,15}$",
            message = "Invalid name , /^[a-zA-Z]\\w{2,15}$/",
            groups = {Insert.class})
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Pattern(regexp = "^1([3-9])\\d{9}$",
            message = "Invalid phone , /^1([3-9])\\d{9}$/",
            groups = {Insert.class, Update.class})
    private String phone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$",
            message = "Invalid email , /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$/",
            groups = {Insert.class, Update.class})
    private String email;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @NotBlank(message = "Password can't be empty",
            groups = {Insert.class, Auth.class})
    @Pattern(regexp = "^[a-zA-Z]\\w{7,15}$",
            message = "Invalid password , /^[a-zA-Z]\\w{7,15}$/",
            groups = {Insert.class, Update.class})
    private String password;

    private Boolean enable;

}
