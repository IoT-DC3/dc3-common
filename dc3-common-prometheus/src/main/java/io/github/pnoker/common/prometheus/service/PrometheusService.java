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

package io.github.pnoker.common.prometheus.service;

import io.github.pnoker.common.exception.RequestException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Prometheus 工具类
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class PrometheusService {

    @Resource
    private OkHttpClient okHttpClient;

    private String sendGetRequest(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful() || response.body() == null) {
                throw new RequestException("Request failed or empty response");
            }
            return response.body().string();
        } catch (Exception e) {
            throw new RequestException(e);
        }
    }

}
