package com.bagautdinov.aop;

import com.bagautdinov.monitoring.MethodMetricsRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodMetricsAspect {

    private final MethodMetricsRegistry methodMetricsRegistry;

    public MethodMetricsAspect(MethodMetricsRegistry methodMetricsRegistry) {
        this.methodMetricsRegistry = methodMetricsRegistry;
    }

    @Around("@annotation(com.bagautdinov.aop.MethodMetric)")
    public Object collect(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = buildMethodName(joinPoint);
        try {
            Object result = joinPoint.proceed();
            methodMetricsRegistry.recordSuccess(methodName);
            return result;
        } catch (Throwable throwable) {
            methodMetricsRegistry.recordFailure(methodName);
            throw throwable;
        }
    }

    private String buildMethodName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getDeclaringType().getSimpleName() + "." + signature.getName();
    }
}
