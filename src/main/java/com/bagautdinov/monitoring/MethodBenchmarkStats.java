package com.bagautdinov.monitoring;

public record MethodBenchmarkStats(
        String methodName,
        int invocationCount,
        double minExecutionTimeMs,
        double maxExecutionTimeMs,
        double averageExecutionTimeMs,
        double totalExecutionTimeMs
) {
}
