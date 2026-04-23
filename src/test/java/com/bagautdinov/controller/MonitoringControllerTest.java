package com.bagautdinov.controller;

import com.bagautdinov.monitoring.BenchmarkPercentileResult;
import com.bagautdinov.monitoring.BenchmarkRegistry;
import com.bagautdinov.monitoring.MethodBenchmarkStats;
import com.bagautdinov.monitoring.MethodExecutionStats;
import com.bagautdinov.monitoring.MethodMetricsRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MonitoringControllerTest {

    private final MethodMetricsRegistry methodMetricsRegistry = new MethodMetricsRegistry();
    private final BenchmarkRegistry benchmarkRegistry = new BenchmarkRegistry();
    private final MonitoringController monitoringController =
            new MonitoringController(methodMetricsRegistry, benchmarkRegistry);

    @Test
    void getExecutionMetricsReturnsAccumulatedStats() {
        methodMetricsRegistry.recordSuccess("UserService.register");
        methodMetricsRegistry.recordFailure("UserService.register");

        List<MethodExecutionStats> result = monitoringController.getExecutionMetrics();

        assertEquals(1, result.size());
        assertEquals("UserService.register", result.get(0).methodName());
        assertEquals(1, result.get(0).successCount());
        assertEquals(1, result.get(0).failureCount());
        assertEquals(2, result.get(0).totalCount());
    }

    @Test
    void getBenchmarkStatsReturnsAggregatedValues() {
        benchmarkRegistry.record("UserService.register", 1_000_000);
        benchmarkRegistry.record("UserService.register", 3_000_000);

        List<MethodBenchmarkStats> result = monitoringController.getBenchmarkStats();

        assertEquals(1, result.size());
        assertEquals("UserService.register", result.get(0).methodName());
        assertEquals(2, result.get(0).invocationCount());
        assertEquals(1.0, result.get(0).minExecutionTimeMs());
        assertEquals(3.0, result.get(0).maxExecutionTimeMs());
        assertEquals(2.0, result.get(0).averageExecutionTimeMs());
        assertEquals(4.0, result.get(0).totalExecutionTimeMs());
    }

    @Test
    void getPercentileReturnsRequestedPercentileForMethod() {
        benchmarkRegistry.record("UserService.register", 1_000_000);
        benchmarkRegistry.record("UserService.register", 2_000_000);
        benchmarkRegistry.record("UserService.register", 5_000_000);

        BenchmarkPercentileResult result = monitoringController.getPercentile("UserService.register", 95);

        assertEquals("UserService.register", result.methodName());
        assertEquals(95.0, result.percentile());
        assertEquals(5.0, result.percentileValueMs());
        assertEquals(3, result.sampleSize());
    }

    @Test
    void getPercentileThrowsForUnknownMethod() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> monitoringController.getPercentile("Unknown.method", 90));

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void getPercentileThrowsForInvalidPercentile() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> monitoringController.getPercentile("UserService.register", 120));

        assertEquals(400, exception.getStatusCode().value());
    }
}
