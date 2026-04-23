package com.bagautdinov.aop;

import com.bagautdinov.monitoring.MethodExecutionStats;
import com.bagautdinov.monitoring.MethodMetricsRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MethodMetricsAspectTest {

    private final MethodMetricsRegistry methodMetricsRegistry = new MethodMetricsRegistry();
    private final MethodMetricsAspect methodMetricsAspect = new MethodMetricsAspect(methodMetricsRegistry);

    @Test
    void collectCountsSuccessfulInvocation() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getDeclaringType()).thenReturn((Class) SampleService.class);
        when(methodSignature.getName()).thenReturn("run");
        when(joinPoint.proceed()).thenReturn("ok");

        Object result = methodMetricsAspect.collect(joinPoint);

        List<MethodExecutionStats> stats = methodMetricsRegistry.getAllStats();
        assertEquals("ok", result);
        assertEquals(1, stats.size());
        assertEquals("SampleService.run", stats.get(0).methodName());
        assertEquals(1, stats.get(0).successCount());
        assertEquals(0, stats.get(0).failureCount());
    }

    @Test
    void collectCountsFailedInvocation() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getDeclaringType()).thenReturn((Class) SampleService.class);
        when(methodSignature.getName()).thenReturn("run");
        when(joinPoint.proceed()).thenThrow(new IllegalStateException("boom"));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> methodMetricsAspect.collect(joinPoint));

        List<MethodExecutionStats> stats = methodMetricsRegistry.getAllStats();
        assertEquals("boom", exception.getMessage());
        assertEquals(1, stats.size());
        assertEquals("SampleService.run", stats.get(0).methodName());
        assertEquals(0, stats.get(0).successCount());
        assertEquals(1, stats.get(0).failureCount());
    }

    private static class SampleService {
        public String run() {
            return "ok";
        }
    }
}
