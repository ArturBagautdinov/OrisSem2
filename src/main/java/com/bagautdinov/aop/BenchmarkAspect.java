package com.bagautdinov.aop;

import com.bagautdinov.monitoring.BenchmarkRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BenchmarkAspect {

    private final BenchmarkRegistry benchmarkRegistry;

    public BenchmarkAspect(BenchmarkRegistry benchmarkRegistry) {
        this.benchmarkRegistry = benchmarkRegistry;
    }

    @Around("@annotation(com.bagautdinov.aop.Benchmark)")
    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = buildMethodName(joinPoint);
        long startedAt = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            benchmarkRegistry.record(methodName, System.nanoTime() - startedAt);
        }
    }

    private String buildMethodName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getDeclaringType().getSimpleName() + "." + signature.getName();
    }
}
