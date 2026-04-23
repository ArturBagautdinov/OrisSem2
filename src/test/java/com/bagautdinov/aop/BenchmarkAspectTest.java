package com.bagautdinov.aop;

import com.bagautdinov.monitoring.BenchmarkRegistry;
import com.bagautdinov.monitoring.MethodBenchmarkStats;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BenchmarkAspectTest {

    private final BenchmarkRegistry benchmarkRegistry = new BenchmarkRegistry();
    private final BenchmarkAspect benchmarkAspect = new BenchmarkAspect(benchmarkRegistry);

    @Test
    void measureStoresExecutionTimeForAnnotatedMethod() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getDeclaringType()).thenReturn((Class) SampleService.class);
        when(methodSignature.getName()).thenReturn("run");
        when(joinPoint.proceed()).thenAnswer(invocation -> "done");

        Object result = benchmarkAspect.measure(joinPoint);

        List<MethodBenchmarkStats> stats = benchmarkRegistry.getAllStats();
        assertEquals("done", result);
        assertEquals(1, stats.size());
        assertEquals("SampleService.run", stats.get(0).methodName());
        assertEquals(1, stats.get(0).invocationCount());
        assertTrue(stats.get(0).totalExecutionTimeMs() >= 0);
    }

    private static class SampleService {
        public String run() {
            return "done";
        }
    }
}
