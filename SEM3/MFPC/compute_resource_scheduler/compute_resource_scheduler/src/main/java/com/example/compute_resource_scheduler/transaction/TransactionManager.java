package com.example.compute_resource_scheduler.transaction;

import com.example.compute_resource_scheduler.exceptions.TransactionAbortedException;
import com.example.compute_resource_scheduler.transaction.enums.DatabaseName;
import com.example.compute_resource_scheduler.transaction.enums.TransactionStatus;
import com.example.compute_resource_scheduler.transaction.enums.UndoEntry;
import com.example.compute_resource_scheduler.transaction.lock.LockManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

@Slf4j
@Component
public class TransactionManager {
    private static final int MAX_RETRIES = 5;

    private final AtomicLong idGenerator = new AtomicLong();
    private final LockManager lockManager;

    private final JdbcTemplate resourcesJdbcTemplate;
    private final JdbcTemplate jobsJdbcTemplate;

    public TransactionManager(LockManager lockManager,
                              @Qualifier("resourcesJdbcTemplate") JdbcTemplate resourcesJdbcTemplate,
                              @Qualifier("jobsJdbcTemplate") JdbcTemplate jobsJdbcTemplate) {
        this.lockManager = lockManager;
        this.resourcesJdbcTemplate = resourcesJdbcTemplate;
        this.jobsJdbcTemplate = jobsJdbcTemplate;
    }

    public Transaction begin() {
        Transaction transaction = new Transaction(idGenerator.incrementAndGet());
        log.info("begin T{}", transaction.getId());
        return transaction;
    }

    public void commit(Transaction transaction) {
        transaction.setStatus(TransactionStatus.COMMITTED);
        transaction.getUndoLog().clear();

        lockManager.releaseAll(transaction);
        log.info("commit T{}", transaction.getId());
    }

    public void rollback(Transaction transaction) {
        while (!transaction.getUndoLog().isEmpty()) {
            UndoEntry operation = transaction.getUndoLog().pop();

            try {
                if (operation.database() == DatabaseName.RESOURCES_DB) {
                    this.resourcesJdbcTemplate.update(operation.sql(), operation.params());
                } else {
                    this.jobsJdbcTemplate.update(operation.sql(), operation.params());
                }
            } catch (Exception exception) {
                log.error("T{}: UNdo failed for [{}]: {}", transaction.getId(), operation.sql(), exception.getMessage());
            }
        }

        transaction.setStatus(TransactionStatus.ABORTED);
        this.lockManager.releaseAll(transaction);

        log.info("rollbacked T{}", transaction.getId());
    }

    public <T> T execute(Function<Transaction, T> task) {
        int attempt = 0;

        while (true) {
            attempt++;
            Transaction transaction = this.begin();

            try {
                T result = task.apply(transaction);
                this.commit(transaction);

                return result;
            } catch (TransactionAbortedException exception) {
                // deadlock victim
                this.rollback(transaction);

                if (attempt >= MAX_RETRIES) {
                    throw exception;
                }

                log.info("restarting after abort (attempt {}): {}", attempt, exception.getMessage());
                sleepRandom();
            } catch (RuntimeException exception) {
                // undo but dont retry
                this.rollback(transaction);

                throw exception;
            }
        }
    }

    private void sleepRandom() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextLong(50, 200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
