package com.bagautdinov.monitoring;

public record BenchmarkPercentileResult(
        String methodName,
        double percentile,
        double percentileValueMs,
        int sampleSize
) {
}
