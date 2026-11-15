package com.example.compute_resource_scheduler.transaction.enums;

public record UndoEntry(DatabaseName database, String sql, Object[] params) {
}
