package com.example.maventraining.hello.service;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GreetingServiceTest {
    @Test
    public void greetsEveryoneInRoster() {
        Roster roster = new Roster(Arrays.asList("Joe", "Bob", "Joe-Bob", "Bobby", "Joey", "Bobby-Joey", "Worthington"));
        GreetingService service = new GreetingService();

        assertEquals(Arrays.asList(
                "Hello, Joe!",
                "Hello, Bob!",
                "Hello, Joe-Bob!",
                "Hello, Bobby!",
                "Hello, Joey!",
                "Hello, Bobby-Joey!",
                "Hello, Worthington!"
        ), service.greet(roster));

    }
}
