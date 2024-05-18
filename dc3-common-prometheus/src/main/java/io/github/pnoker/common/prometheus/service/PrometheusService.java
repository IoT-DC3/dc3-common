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
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * Prometheus 工具类
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class PrometheusService {

    @Value("${prometheus.api.query}")
    private String queryApiUrl;

    @Value("${prometheus.api.query-range}")
    private String queryRangeApiUrl;

    @Resource
    private OkHttpClient okHttpClient;

    /**
     * 查询
     *
     * @param params Parameter Map
     * @return String of Response Body
     */
    public String query(Map<String, String> params) {
        return getString(queryApiUrl, params);
    }

    /**
     * 查询
     *
     * @param params Parameter Map
     * @return String of Response Body
     */
    public String queryRange(Map<String, String> params) {
        return getRangeString(queryRangeApiUrl, params);
    }

    /**
     * Prometheus Query 查询接口
     *
     * @param api    Api URL
     * @param params Parameter Map
     * @return String of Response Body
     */
    private String getString(String api, Map<String, String> params) {
        try {
            HttpUrl url = HttpUrl.parse(api);
            if (Objects.isNull(url)) {
                throw new RequestException("Request url empty");
            }
            HttpUrl.Builder builder = url.newBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            Request request = new Request.Builder()
                    .url(builder.build())
                    .get()
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful() || Objects.isNull(response.body())) {
                throw new RequestException("Request failed or empty response");
            }
            return response.body().string();
        } catch (Exception e) {
            throw new RequestException(e);
        }
    }

    private String getRangeString(String api, Map<String, String> params) {
        try {
            HttpUrl url = HttpUrl.parse(api);
            if (Objects.isNull(url)) {
                throw new RequestException("Request url empty");
            }
            HttpUrl.Builder builder = url.newBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            // 获取当前时间并转换为 UTC 时间
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            // 计算前 15 分钟之前的时间并转换为 UTC 时间
            LocalDateTime fifteenMinutesAgo = now.minusMinutes(15);
            long time1 = fifteenMinutesAgo.toEpochSecond(ZoneOffset.UTC);
            long time2 = now.toEpochSecond(ZoneOffset.UTC);
            String rangeUrl = builder.build() + "&start=" + time1 + "&end=" + time2 + "&step=15";
            Request request = new Request.Builder()
                    .url(rangeUrl)
                    .get()
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful() || Objects.isNull(response.body())) {
                throw new RequestException("Request failed or empty response");
            }
            return response.body().string();
        } catch (Exception e) {
            throw new RequestException(e);
        }
    }

    public String UnTimeUnix(Double dtime) {
        // 将 UNIX 时间戳转换为 Instant 对象
        Instant instant = Instant.ofEpochSecond(Math.round(dtime));// 2024-03-18 08:04:00
        // 将 Instant 对象转换为 LocalDateTime 对象, 使用默认时区
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化日期和时间
        String formattedDateTime = dateTime.format(formatter);
        return formattedDateTime;
    }

}
