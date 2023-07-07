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

package io.github.pnoker.common.config;

import io.github.pnoker.common.constant.common.RequestConstant;
import io.github.pnoker.common.entity.bo.AuthInfoBO;
import io.github.pnoker.common.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web 配置
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
                // 配置权限信息
                AuthInfoBO authInfo = getAuthInfo(request);
                RequestUtil.setAuthInfo(authInfo);
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }

            @Override
            public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) throws Exception {
                HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
            }

            @Override
            public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
                // 清除权限信息
                RequestUtil.resetAuthInfo();
                HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
            }
        });
    }

    /**
     * 从 Request Header 中获取权限信息
     *
     * @param request {@link HttpServletRequest}
     * @return {@link AuthInfoBO}
     */
    private AuthInfoBO getAuthInfo(HttpServletRequest request) {
        String tenantId = RequestUtil.getRequestHeader(request, RequestConstant.Header.X_AUTH_TENANT_ID);
        String tenantName = RequestUtil.getRequestHeader(request, RequestConstant.Header.X_AUTH_TENANT);
        String userId = RequestUtil.getRequestHeader(request, RequestConstant.Header.X_AUTH_USER_ID);
        String nickName = RequestUtil.getRequestHeader(request, RequestConstant.Header.X_AUTH_NICK);
        String userName = RequestUtil.getRequestHeader(request, RequestConstant.Header.X_AUTH_USER);
        return new AuthInfoBO(tenantId, tenantName, userId, nickName, userName);
    }
}