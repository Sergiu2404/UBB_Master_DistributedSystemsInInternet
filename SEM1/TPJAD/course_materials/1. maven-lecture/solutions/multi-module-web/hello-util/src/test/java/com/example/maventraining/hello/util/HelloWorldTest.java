package com.example.maventraining.hello.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldTest {
    @Test
    public void testSomething() {
        HelloWorld helloWorld = new HelloWorld();
        assertEquals("Hello, Dave!", helloWorld.hello("Dave"));
    }
}
