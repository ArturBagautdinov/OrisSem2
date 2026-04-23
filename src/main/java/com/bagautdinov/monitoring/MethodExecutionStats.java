package com.bagautdinov.monitoring;

public record MethodExecutionStats(
        String methodName,
        long successCount,
        long failureCount
) {
    public long totalCount() {
        return successCount + failureCount;
    }
}
