package com.example.javacore.threads.classic.threadsafety.services;

public class MessageService {
    
    private final String message;
    
    public MessageService(String message) {
        this.message = message;
    }
    
    public String getMesssage() {
        return message;
    }
}
