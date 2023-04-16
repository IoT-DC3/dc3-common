package io.github.pnoker.common.annotation;

import io.github.pnoker.common.enums.AuthMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author linys
 * @since 2023.04.08
 *
 * eg. @CheckPermission("resource code"), @CheckPermission({"resource code","resource code"})
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface CheckPermission {

    /**
     * resource code array
     * @return resources code
     */
    String [] value() default {};

    /**
     * resource check mode
     * @return mode
     */
    AuthMode mode() default AuthMode.AND;

    /**
     * role code array
     * satisfy one of resources and roles
     * @return role code
     */
    String[] role() default {};

    /**
     * role check mode
     * @return mode
     */
    AuthMode roleMode() default AuthMode.AND;
}
