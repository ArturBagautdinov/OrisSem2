package com.bagautdinov.controller;

import com.bagautdinov.monitoring.BenchmarkPercentileResult;
import com.bagautdinov.monitoring.BenchmarkRegistry;
import com.bagautdinov.monitoring.MethodBenchmarkStats;
import com.bagautdinov.monitoring.MethodExecutionStats;
import com.bagautdinov.monitoring.MethodMetricsRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/metrics")
public class MonitoringController {

    private final MethodMetricsRegistry methodMetricsRegistry;
    private final BenchmarkRegistry benchmarkRegistry;

    public MonitoringController(MethodMetricsRegistry methodMetricsRegistry,
                                BenchmarkRegistry benchmarkRegistry) {
        this.methodMetricsRegistry = methodMetricsRegistry;
        this.benchmarkRegistry = benchmarkRegistry;
    }

    @GetMapping("/execution")
    public List<MethodExecutionStats> getExecutionMetrics() {
        return methodMetricsRegistry.getAllStats();
    }

    @GetMapping("/benchmarks")
    public List<MethodBenchmarkStats> getBenchmarkStats() {
        return benchmarkRegistry.getAllStats();
    }

    @GetMapping("/benchmarks/percentile")
    public BenchmarkPercentileResult getPercentile(@RequestParam("method") String methodName,
                                                   @RequestParam("n") double percentile) {
        if (percentile < 0 || percentile > 100) {
            throw new ResponseStatusException(BAD_REQUEST, "Percentile n must be between 0 and 100");
        }

        return benchmarkRegistry.calculatePercentile(methodName, percentile)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND,
                        "Benchmark stats not found for method: " + methodName
                ));
    }
}
