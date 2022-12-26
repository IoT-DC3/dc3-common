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
import io.github.pnoker.common.valid.Insert;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * Rtmp
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Rtmp extends BaseModel {

    @NotBlank(message = "Name can't be empty", groups = {Insert.class})
    private String name;

    @NotBlank(message = "Rtsp url can't be empty", groups = {Insert.class})
    private String rtspUrl;

    @NotBlank(message = "Rtmp url can't be empty", groups = {Insert.class})
    private String rtmpUrl;

    @NotBlank(message = "Command can't be empty", groups = {Insert.class})
    private String command;

    private Short videoType;
    private Boolean run;
    private Boolean autoStart;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String tenantId;

    public Rtmp(String id, boolean run) {
        super.setId(id);
        this.run = run;
    }

    public Rtmp(boolean autoStart) {
        this.autoStart = autoStart;
    }

}