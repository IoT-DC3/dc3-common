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

package io.github.pnoker.common.config;

import cn.hutool.core.text.CharSequenceUtil;
import io.github.pnoker.common.constant.common.RequestConstant;
import io.github.pnoker.common.entity.common.RequestHeader;
import io.github.pnoker.common.utils.DecodeUtil;
import io.github.pnoker.common.utils.HeaderUtil;
import io.github.pnoker.common.utils.JsonUtil;
import io.github.pnoker.common.utils.UserHeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.WebFilter;

/**
 * WebFilter 配置
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Configuration
public class WebFilterConfig {

    @Bean
    public WebFilter interceptor() {
        return (exchange, chain) -> {
            //setUserHeader(exchange.getRequest());
//            return chain.filter(exchange).then(Mono.fromRunnable(UserHeaderUtil::removeUserHeader));
            return chain.filter(exchange);
        };
    }

    private void setUserHeader(ServerHttpRequest request) {
        try {
            String user = HeaderUtil.getRequestHeader(request, RequestConstant.Header.X_AUTH_USER);
            if (CharSequenceUtil.isEmpty(user)) {
                return;
            }
            byte[] decode = DecodeUtil.decode(user);
            RequestHeader.UserHeader entityBO = JsonUtil.parseObject(decode, RequestHeader.UserHeader.class);
            UserHeaderUtil.setUserHeader(entityBO);
        } catch (Exception ignored) {
        }
    }
}