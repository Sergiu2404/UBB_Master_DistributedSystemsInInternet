package com.example.maventraining.hello.service;
import com.example.maventraining.hello.util.HelloWorld;

import java.util.List;
import java.util.stream.Collectors;

public class GreetingService {
    private HelloWorld helloWorld = new HelloWorld();

    public List<String> greet(Roster roster) {
        return roster.getNames().stream()
                .map(name -> helloWorld.hello(name))
                .collect(Collectors.toList());
    }
}
