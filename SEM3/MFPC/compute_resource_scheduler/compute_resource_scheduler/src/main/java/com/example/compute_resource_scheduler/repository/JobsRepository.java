package com.example.compute_resource_scheduler.repository;

import com.example.compute_resource_scheduler.helpers.constants.DatabaseQueries;
import com.example.compute_resource_scheduler.helpers.constants.DatabaseTables;
import com.example.compute_resource_scheduler.models.jobs.Allocation;
import com.example.compute_resource_scheduler.models.jobs.Job;
import com.example.compute_resource_scheduler.transaction.Transaction;
import com.example.compute_resource_scheduler.transaction.enums.DatabaseName;
import com.example.compute_resource_scheduler.transaction.enums.LockMode;
import com.example.compute_resource_scheduler.transaction.enums.UndoEntry;
import com.example.compute_resource_scheduler.transaction.lock.LockEntry;
import com.example.compute_resource_scheduler.transaction.lock.LockManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JobsRepository {
    private final JdbcTemplate jdbcTemplate;
    private final LockManager lockManager;

    public JobsRepository(@Qualifier("jobsJdbcTemplate") JdbcTemplate jdbcTemplate, LockManager lockManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.lockManager = lockManager;
    }

    public Job saveJob(Transaction transaction, Job job) {
        if (job.getId() == null) {
            job.setId(UUID.randomUUID());
        }
        if (job.getStatus() == null) {
            job.setStatus("pending");
        }

        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.JOBS_DB, DatabaseTables.JOBS_TABLE, job.getId()),
                LockMode.EXCLUSIVE
        );

        transaction.getUndoLog().push(new UndoEntry(
                DatabaseName.JOBS_DB,
                DatabaseQueries.DELETE_JOB_BY_ID,
                new Object[] { job.getId() }
        ));

        String insert = DatabaseQueries.INSERT_JOB;

        try {
            this.jdbcTemplate.update(
                    insert,
                    job.getId(),
                    job.getName(),
                    job.getRequiredCpuCores(),
                    job.getRequiredMemoryMb(),
                    job.getPriority(),
                    job.getStatus()
            );

            return job;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save job: " + e.getMessage(), e);
        }
    }

    public Optional<Job> findJobById(Transaction transaction, LockMode lockMode, UUID id) {
        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.JOBS_DB, DatabaseTables.JOBS_TABLE, id),
                lockMode
        );

        String sql = DatabaseQueries.SELECT_JOB_BY_ID;

        try {
            Job job = this.jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(Job.class),
                    id
            );

            return Optional.ofNullable(job);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Job> findAllJobs() {
        String sql = DatabaseQueries.SELECT_JOBS;

        try {
            return this.jdbcTemplate.query(
                    sql,
                    new BeanPropertyRowMapper<>(Job.class)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch jobs list: " + e.getMessage(), e);
        }
    }

    public List<Allocation> findAllAlocations() {
        String sql = DatabaseQueries.SELECT_ALLOCATIONS;

        try {
            return this.jdbcTemplate.query(
                    sql,
                    new BeanPropertyRowMapper<>(Allocation.class)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch jobs list: " + e.getMessage(), e);
        }
    }

    public List<Allocation> findAllocationsByJob(UUID jobId) {
        String sql = DatabaseQueries.SELECT_ALLOCATIONS_BY_JOB_ID;

        try {
            return this.jdbcTemplate.query(
                    sql,
                    new BeanPropertyRowMapper<>(Allocation.class),
                    jobId
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch allocations of compute node: " + e.getMessage(), e);
        }
    }

    public List<Allocation> findAllocationsByNode(UUID nodeId) {
        String sql = DatabaseQueries.SELECT_ALLOCATIONS_BY_COMPUTE_NODE_ID;

        try {
            return this.jdbcTemplate.query(
                    sql,
                    new BeanPropertyRowMapper<>(Allocation.class),
                    nodeId
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch allocations of compute node: " + e.getMessage(), e);
        }
    }

    public void updateJobStatus(Transaction transaction, UUID jobId, String newStatus) {
        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.JOBS_DB, DatabaseTables.JOBS_TABLE, jobId),
                LockMode.EXCLUSIVE
        );

        String oldStatus = this.jdbcTemplate.queryForObject(
                DatabaseQueries.SELECT_STATUS_FROM_JOB_BY_ID,
                String.class,
                jobId
        );

        transaction.getUndoLog().push(new UndoEntry(
                DatabaseName.JOBS_DB,
                DatabaseQueries.UPDATE_STATUS_FOR_JOB_BY_ID,
                new Object[] { oldStatus, jobId }
        ));

        try {
            this.jdbcTemplate.update(DatabaseQueries.UPDATE_STATUS_FOR_JOB_BY_ID, newStatus, jobId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update job status: " + e.getMessage(), e);
        }
    }

    public Allocation insertAllocation(Transaction transaction, Allocation allocation) {
        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.JOBS_DB, DatabaseTables.ALLOCATIONS_TABLE, allocation.getId()),
                LockMode.EXCLUSIVE
        );

        transaction.getUndoLog().push(new UndoEntry(
                DatabaseName.JOBS_DB,
                DatabaseQueries.DELETE_ALLOCATION_BY_ID,
                new Object[] { allocation.getId() }
        ));

        try {
            this.jdbcTemplate.update(DatabaseQueries.INSERT_ALLOCATION,
                    allocation.getId(),
                    allocation.getJobId(),
                    allocation.getComputeNodeId(),
                    allocation.getAllocatedCpuCores(),
                    allocation.getAllocatedMemoryMb(),
                    allocation.getStatus()
            );

            return allocation;
        } catch (Exception e) {
            throw new RuntimeException("Failed to insert allocation: " + e.getMessage(), e);
        }
    }

    public Optional<Allocation> findActiveAlocationByJob(Transaction transaction, UUID jobId) {
        List<Allocation> allocations = this.jdbcTemplate.query(
                DatabaseQueries.SELECT_ACTIVE_ALLOCATIONS_BY_JOB_ID,
                new BeanPropertyRowMapper<>(Allocation.class),
                jobId
        );

        if (allocations.isEmpty()) {
            return Optional.empty();
        }

        Allocation allocation = allocations.getFirst();

        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.JOBS_DB, DatabaseTables.ALLOCATIONS_TABLE, allocation.getId()),
                LockMode.EXCLUSIVE
        );

        return Optional.of(allocation);
    }

    public List<Allocation> findActiveAlocationsByNode(Transaction transaction, UUID nodeId) {
        String sql = DatabaseQueries.SELECT_ACTIVE_ALLOCATIONS_BY_COMPUTE_NODE_ID;

        List<Allocation> candidates = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Allocation.class), nodeId);

        for (Allocation allocation : candidates) {
            this.lockManager.acquireLock(
                    transaction,
                    LockManager.resource(DatabaseName.JOBS_DB, DatabaseTables.ALLOCATIONS_TABLE, allocation.getId()),
                    LockMode.EXCLUSIVE
            );
        }

        return this.jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Allocation.class),
                nodeId
        );
    }

    public void releaseAlocation(Transaction transaction, UUID allocationId) {
        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.JOBS_DB, DatabaseTables.ALLOCATIONS_TABLE, allocationId),
                LockMode.EXCLUSIVE
        );

        Map<String, Object> oldAllocation = this.jdbcTemplate.queryForMap(
                DatabaseQueries.SELECT_STATUS_AND_RELEASED_TIMESTAMP_FROM_ALLOCATION_BY_ID,
                allocationId
        );

        transaction.getUndoLog().push(new UndoEntry(
                DatabaseName.JOBS_DB,
                DatabaseQueries.UPDATE_STATUS_AND_RELEASED_TIMESTAMP_FOR_ALLOCATION_BY_ID,
                new Object[] { oldAllocation.get("status"), oldAllocation.get("released_at"), allocationId }
        ));

        try {
            this.jdbcTemplate.update(
                    DatabaseQueries.UPDATE_STATUS_AND_RELEASED_TIMESTAMP_TO_NOW_FOR_ALLOCATION_BY_ID,
                    allocationId
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to release allocation: " + e.getMessage(), e);
        }
    }
}
