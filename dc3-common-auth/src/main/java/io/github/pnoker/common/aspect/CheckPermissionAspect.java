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

package io.github.pnoker.common.aspect;

import cn.hutool.core.collection.CollUtil;
import io.github.pnoker.common.annotation.CheckPermission;
import io.github.pnoker.common.constant.AuthConstant;
import io.github.pnoker.common.enums.AuthMode;
import io.github.pnoker.common.exception.UnAuthorizedException;
import io.github.pnoker.common.model.AuthUser;
import io.github.pnoker.common.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * @author linys
 * @since 2022.1.0
 */
@Slf4j
@Aspect
@Component
public class CheckPermissionAspect {
    @Pointcut("@annotation(io.github.pnoker.common.annotation.CheckPermission)")
    public void permissionCut() {
    }

    @Before("permissionCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        CheckPermission annotation = methodSignature.getMethod().getAnnotation(CheckPermission.class);

        AuthUser authUser = AuthUtil.getAuthUser();

        //1. check role
        if (checkRole(annotation, authUser)) {
            return;
        }

        //2. check permission
        if (!checkResource(annotation, authUser)) {
            throw new UnAuthorizedException("No access");
        }
    }

    private Boolean checkRole(CheckPermission annotation, AuthUser authUser) {
        String[] requireRoles = annotation.role();
        if (Objects.isNull(requireRoles) || requireRoles.length == 0) {
            return Boolean.FALSE;
        }

        Set<String> roleCodeSet = authUser.getRoleCodeSet();
        if (CollUtil.isEmpty(roleCodeSet)) {
            return Boolean.FALSE;
        }
        if (roleCodeSet.contains(AuthConstant.role_code_admin)) {
            return Boolean.TRUE;
        }

        if (AuthMode.AND.equals(annotation.roleMode())) {
            return checkAndMode(requireRoles, roleCodeSet);
        } else {
            return checkOrMode(requireRoles, roleCodeSet);
        }
    }

    private Boolean checkResource(CheckPermission annotation, AuthUser authUser) {
        String[] requireResources = annotation.value();
        if (Objects.isNull(requireResources) || requireResources.length == 0) {
            return Boolean.FALSE;
        }

        Set<String> resourceCodeSet = authUser.getResourceCodeSet();
        if (CollUtil.isEmpty(resourceCodeSet)) {
            return Boolean.FALSE;
        }

        if (AuthMode.AND.equals(annotation.mode())) {
            //check and mode
            return checkAndMode(requireResources, resourceCodeSet);
        } else {
            //check or mode
            return checkOrMode(requireResources, resourceCodeSet);
        }
    }

    private Boolean checkAndMode(String[] requirePermissions, Set<String> hasPermissions) {
        for (String requirePermission : requirePermissions) {
            if (!hasPermissions.contains(requirePermission)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private Boolean checkOrMode(String[] requirePermissions, Set<String> hasPermissions) {
        for (String requirePermission : requirePermissions) {
            if (hasPermissions.contains(requirePermission)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
