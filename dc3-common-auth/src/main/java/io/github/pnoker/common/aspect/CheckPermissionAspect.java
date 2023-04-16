package io.github.pnoker.common.aspect;

import io.github.pnoker.common.annotation.CheckPermission;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author linys
 * @since 2023.04.08
 */
@Slf4j
@Aspect
@Component
public class CheckPermissionAspect {
    @Pointcut("@annotation(io.github.pnoker.common.annotation.CheckPermission)")
    public void permissionCut() {
    }

    @Around("permissionCut() && @annotation(checkPermission)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, CheckPermission checkPermission) throws Throwable {
        String className = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        try {
            //1. check annotation params

            //2. do check permission

            Object proceed = proceedingJoinPoint.proceed();
            return proceed;
        } catch (Throwable throwable) {
            throw throwable;
        }
    }
}
