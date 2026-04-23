package com.bagautdinov.monitoring;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class BenchmarkRegistry {

    private final Map<String, ConcurrentLinkedQueue<Long>> benchmarkData = new ConcurrentHashMap<>();

    public void record(String methodName, long durationNanos) {
        benchmarkData.computeIfAbsent(methodName, ignored -> new ConcurrentLinkedQueue<>())
                .add(durationNanos);
    }

    public List<MethodBenchmarkStats> getAllStats() {
        return benchmarkData.entrySet().stream()
                .map(entry -> toStats(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(MethodBenchmarkStats::methodName))
                .toList();
    }

    public Optional<BenchmarkPercentileResult> calculatePercentile(String methodName, double percentile) {
        ConcurrentLinkedQueue<Long> durations = benchmarkData.get(methodName);
        if (durations == null || durations.isEmpty()) {
            return Optional.empty();
        }

        List<Long> sortedDurations = new ArrayList<>(durations);
        sortedDurations.sort(Long::compareTo);

        int index = percentile == 0
                ? 0
                : (int) Math.ceil(percentile / 100.0 * sortedDurations.size()) - 1;

        long duration = sortedDurations.get(Math.max(index, 0));
        return Optional.of(new BenchmarkPercentileResult(
                methodName,
                percentile,
                nanosToMillis(duration),
                sortedDurations.size()
        ));
    }

    private MethodBenchmarkStats toStats(String methodName, ConcurrentLinkedQueue<Long> durations) {
        List<Long> snapshot = new ArrayList<>(durations);
        long min = snapshot.stream().mapToLong(Long::longValue).min().orElse(0L);
        long max = snapshot.stream().mapToLong(Long::longValue).max().orElse(0L);
        long total = snapshot.stream().mapToLong(Long::longValue).sum();
        double average = snapshot.isEmpty() ? 0 : (double) total / snapshot.size();

        return new MethodBenchmarkStats(
                methodName,
                snapshot.size(),
                nanosToMillis(min),
                nanosToMillis(max),
                nanosToMillis(average),
                nanosToMillis(total)
        );
    }

    private double nanosToMillis(long nanos) {
        return nanos / 1_000_000.0;
    }

    private double nanosToMillis(double nanos) {
        return nanos / 1_000_000.0;
    }
}
