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

package io.github.pnoker.common.utils;

import io.github.pnoker.common.constant.common.ExceptionConstant;
import io.github.pnoker.common.exception.NotFoundException;
import io.github.pnoker.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

/**
 * Request 相关工具类
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
public class RequestUtil {

    private RequestUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 从 Request 中获取指定 Key 的 Header 值
     *
     * @param request HttpServletRequest
     * @param key     String
     * @return String
     */
    public static String getRequestHeader(HttpServletRequest request, String key) {
        return request.getHeader(key);
    }

    /**
     * 返回文件
     *
     * @param path 文件 Path
     * @return Resource
     * @throws MalformedURLException MalformedURLException
     */
    public static ResponseEntity<Resource> responseFile(Path path) throws MalformedURLException {
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists()) {
            throw new NotFoundException("File not found: " + path.getFileName());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        try {
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (IOException e) {
            throw new ServiceException("Error occurred while response file: {}", path.getFileName());
        }
    }
}
