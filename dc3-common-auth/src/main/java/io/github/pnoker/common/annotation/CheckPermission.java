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

package io.github.pnoker.common.annotation;

import io.github.pnoker.common.enums.AuthMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author linys
 * @since 2022.1.0
 * <p>
 * eg. @CheckPermission("resource code"), @CheckPermission({"resource code","resource code"})
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CheckPermission {

    /**
     * resource code array
     *
     * @return resources code
     */
    String[] value() default {};

    /**
     * resource check mode
     *
     * @return mode
     */
    AuthMode mode() default AuthMode.AND;

    /**
     * role code array
     * satisfy one of resources and roles
     *
     * @return role code
     */
    String[] role() default {};

    /**
     * role check mode
     *
     * @return mode
     */
    AuthMode roleMode() default AuthMode.AND;
}
