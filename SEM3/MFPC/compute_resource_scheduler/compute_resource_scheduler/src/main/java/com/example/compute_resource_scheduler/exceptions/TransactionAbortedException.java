package com.example.compute_resource_scheduler.exceptions;

public class TransactionAbortedException extends RuntimeException {
    public TransactionAbortedException(String message) {
        super(message);
    }
}
