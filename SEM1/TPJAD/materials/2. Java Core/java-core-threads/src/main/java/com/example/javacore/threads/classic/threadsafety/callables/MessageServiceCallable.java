package com.example.javacore.threads.classic.threadsafety.callables;

import com.example.javacore.threads.classic.threadsafety.services.MessageService;

import java.util.concurrent.Callable;

public class MessageServiceCallable implements Callable<String> {

    private final MessageService messageService;

    public MessageServiceCallable(MessageService messageService) {
        this.messageService = messageService;

    }

    @Override
    public String call() {
        return messageService.getMesssage();
    }
}
