package com.bagautdinov.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloServiceTest {

    private final HelloService helloService = new HelloService();

    @Test
    void sayHelloFormatsProvidedName() {
        assertEquals("Hello, Ivan", helloService.sayHello("Ivan"));
    }

    @Test
    void sayHelloHandlesNullValue() {
        assertEquals("Hello, null", helloService.sayHello(null));
    }
}
