package com.example.compute_resource_scheduler.transaction.lock;

import com.example.compute_resource_scheduler.exceptions.TransactionAbortedException;
import com.example.compute_resource_scheduler.transaction.Transaction;
import com.example.compute_resource_scheduler.transaction.deadlock_management.WaitForGraph;
import com.example.compute_resource_scheduler.transaction.enums.DatabaseName;
import com.example.compute_resource_scheduler.transaction.enums.LockMode;
import com.example.compute_resource_scheduler.transaction.enums.TransactionStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LockManager {
    private final Map<String, LockEntry> locks = new HashMap<>();
    private final Map<Long, Transaction> activeTransactions = new HashMap<>();

    private final WaitForGraph waitForGraph = new WaitForGraph();

    public synchronized void acquireLock(Transaction transaction, String resource, LockMode mode) {
        activeTransactions.putIfAbsent(transaction.getId(), transaction);

        while (true) {
            if (transaction.getStatus() == TransactionStatus.ABORTED) {
                this.waitForGraph.removeWaitEdges(transaction.getId());

                throw new TransactionAbortedException(String.format("Transaction with id %d was aborted!", transaction.getId()));
            }

            LockEntry lock = locks.computeIfAbsent(resource, res -> new LockEntry());

            if (this.canGrantLock(lock, transaction.getId(), mode)) {
                this.grantLock(lock, transaction, resource, mode);
                this.waitForGraph.removeWaitEdges(transaction.getId());
                return;
            }

            this.waitForLock(transaction, lock);

            try {
                wait(); // releases the monitor until some thr calls notifyAll
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new TransactionAbortedException("Interrupted while waiting for " + resource);
            }
        }
    }

    public synchronized void releaseAll(Transaction transaction) {
        for (String resource : transaction.getHeldLocks()) {
            Set<Long> holderTransactions = this.locks.get(resource).getHolderTransactions();
            holderTransactions.remove(transaction.getId());

            if (holderTransactions.isEmpty()) {
                this.locks.remove(resource);
            }
        }

        transaction.getHeldLocks().clear();

        this.waitForGraph.removeTransaction(transaction.getId());
        this.activeTransactions.remove(transaction.getId());

        notifyAll();
    }

    public static String resource(DatabaseName database, String table, Object rowId) {
        return database + ":" + table + ":" + rowId;
    }

    private void waitForLock(Transaction transaction, LockEntry lock) {
        this.waitForGraph.addWaitEdges(transaction.getId(), lock.getHolderTransactions());
        List<Long> cycle = this.waitForGraph.findCycleFrom(transaction.getId());

        if (!cycle.isEmpty()) {
            long victimId = Collections.max(cycle); // most recently created

            if (victimId == transaction.getId()) {
                waitForGraph.removeWaitEdges(transaction.getId());
                transaction.setStatus(TransactionStatus.ABORTED);

                throw new TransactionAbortedException("Transaction " + transaction.getId() + " aborted: deadlock " + cycle);
            }

            activeTransactions.get(victimId).setStatus(TransactionStatus.ABORTED);
            notifyAll(); // wake the victim so it sees ABORTED and throws
        }
    }

    private boolean canGrantLock(LockEntry lock, long transactionId, LockMode mode) {
        if (lock.getHolderTransactions() == null || lock.getHolderTransactions().isEmpty()) {
            return true; //acquire if not already used by other transactions
        }

        if (lock.getHolderTransactions().size() == 1 && lock.getHolderTransactions().iterator().next() == transactionId) {
            return true;
        }

        if (mode == LockMode.SHARED && lock.getMode() == LockMode.SHARED) {
            return true;
        }

        return false;
    }

    private void grantLock(LockEntry lock, Transaction transaction, String resource, LockMode mode) {
        lock.getHolderTransactions().add(transaction.getId());
        transaction.getHeldLocks().add(resource);

        if (mode == LockMode.EXCLUSIVE) {
            lock.setMode(LockMode.EXCLUSIVE);
        }
        else if (lock.getMode() == null) {
            lock.setMode(LockMode.SHARED);
        }
    }
}
