package com.example.maventraining.hello.service;
import com.example.maventraining.hello.util.AnotherHelloWorld;

import java.util.List;
import java.util.stream.Collectors;

public class GreetingService {
    private AnotherHelloWorld anotherHelloWorld = new AnotherHelloWorld();

    public List<String> greet(Roster roster) {
        return roster.getNames().stream()
                .map(name -> anotherHelloWorld.hello(name))
                .collect(Collectors.toList());
    }
}
