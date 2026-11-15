package com.example.compute_resource_scheduler.transaction;

import com.example.compute_resource_scheduler.transaction.enums.TransactionStatus;
import com.example.compute_resource_scheduler.transaction.enums.UndoEntry;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Transaction {
    private final long id;

    private final OffsetDateTime startTimestamp = OffsetDateTime.now();

    @Setter
    private volatile TransactionStatus status = TransactionStatus.ACTIVE;

    private final Set<String> heldLocks = new HashSet<>();

    private final Deque<UndoEntry> undoLog = new ArrayDeque<>();

    public Transaction(long id) {
        this.id = id;
    }
}
