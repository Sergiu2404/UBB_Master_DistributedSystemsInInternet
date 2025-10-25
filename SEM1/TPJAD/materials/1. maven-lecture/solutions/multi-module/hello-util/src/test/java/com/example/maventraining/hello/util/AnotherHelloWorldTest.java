package com.example.maventraining.hello.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnotherHelloWorldTest {
    @Test
    public void testSomething() {
        AnotherHelloWorld anotherHelloWorld = new AnotherHelloWorld();
        assertEquals("Hello, Ionut!", anotherHelloWorld.hello("Ionut"));
    }
}
