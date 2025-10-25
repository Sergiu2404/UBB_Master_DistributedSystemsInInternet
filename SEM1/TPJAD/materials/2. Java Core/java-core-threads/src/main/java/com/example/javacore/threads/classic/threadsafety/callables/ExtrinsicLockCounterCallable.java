package com.example.javacore.threads.classic.threadsafety.callables;

import com.example.javacore.threads.classic.threadsafety.services.ObjectLockCounter;

import java.util.concurrent.Callable;

public class ExtrinsicLockCounterCallable implements Callable<Integer> {

    private final ObjectLockCounter counter;
    
    public ExtrinsicLockCounterCallable(ObjectLockCounter counter) {
        this.counter = counter;
    }
    
    @Override
    public Integer call() throws Exception {
        counter.incrementCounter();
        return counter.getCounter();
    }
}
