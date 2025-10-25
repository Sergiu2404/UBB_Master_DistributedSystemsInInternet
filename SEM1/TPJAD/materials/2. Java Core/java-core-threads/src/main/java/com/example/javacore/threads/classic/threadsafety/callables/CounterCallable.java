package com.example.javacore.threads.classic.threadsafety.callables;

import com.example.javacore.threads.classic.threadsafety.services.Counter;

import java.util.concurrent.Callable;

public class CounterCallable implements Callable<Integer> {

    private final Counter counter;
    
    public CounterCallable(Counter counter) {
        this.counter = counter;
    }

    @Override
    public Integer call() throws Exception {
        counter.incrementCounter();
        return counter.getCounter();
    }
}
