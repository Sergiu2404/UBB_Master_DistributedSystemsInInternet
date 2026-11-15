package com.example.compute_resource_scheduler.transaction.lock;

import com.example.compute_resource_scheduler.transaction.enums.LockMode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class LockEntry {
    private LockMode mode;

    private final Set<Long> holderTransactions = new HashSet<>();
}
