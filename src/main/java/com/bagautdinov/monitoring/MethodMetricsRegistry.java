package com.bagautdinov.monitoring;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@Component
public class MethodMetricsRegistry {

    private final Map<String, MethodCounters> methodCounters = new ConcurrentHashMap<>();

    public void recordSuccess(String methodName) {
        methodCounters.computeIfAbsent(methodName, ignored -> new MethodCounters()).success().increment();
    }

    public void recordFailure(String methodName) {
        methodCounters.computeIfAbsent(methodName, ignored -> new MethodCounters()).failure().increment();
    }

    public List<MethodExecutionStats> getAllStats() {
        return methodCounters.entrySet().stream()
                .map(entry -> new MethodExecutionStats(
                        entry.getKey(),
                        entry.getValue().success().sum(),
                        entry.getValue().failure().sum()
                ))
                .sorted(Comparator.comparing(MethodExecutionStats::methodName))
                .toList();
    }

    private record MethodCounters(LongAdder success, LongAdder failure) {
        private MethodCounters() {
            this(new LongAdder(), new LongAdder());
        }
    }
}
