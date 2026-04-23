package com.bagautdinov.service;

import com.bagautdinov.aop.Benchmark;
import com.bagautdinov.aop.MethodMetric;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    @MethodMetric
    @Benchmark
    public String sayHello(String name) {
        return "Hello, %s".formatted(name);
    }
}
