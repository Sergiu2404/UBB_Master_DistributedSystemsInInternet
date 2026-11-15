package com.example.compute_resource_scheduler.repository;

import com.example.compute_resource_scheduler.helpers.constants.DatabaseQueries;
import com.example.compute_resource_scheduler.helpers.constants.DatabaseTables;
import com.example.compute_resource_scheduler.models.resources.ComputeNode;
import com.example.compute_resource_scheduler.models.resources.ResourcePool;
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
public class ResourcesRepository {
    private final JdbcTemplate jdbcTemplate;
    private final LockManager lockManager;

    public ResourcesRepository(@Qualifier("resourcesJdbcTemplate") JdbcTemplate jdbcTemplate, LockManager lockManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.lockManager = lockManager;
    }

    public ResourcePool saveResourcePool(Transaction transaction, ResourcePool pool) {
        if (pool.getId() == null) {
            pool.setId(UUID.randomUUID());
        }

        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.RESOURCES_DB, DatabaseTables.RESOURCE_POOLS_TABLE, pool.getId()),
                LockMode.EXCLUSIVE
        );

        transaction.getUndoLog().push(new UndoEntry(
                DatabaseName.RESOURCES_DB,
                DatabaseQueries.DELETE_RESOURCE_BY_ID,
                new Object[] { pool.getId() }
        ));

        String sql = DatabaseQueries.INSERT_RESOURCE;

        try {
            this.jdbcTemplate.update(
                    sql,
                    pool.getId(),
                    pool.getName(),
                    pool.getRegion()
            );

            return pool;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save resource pool: " + e.getMessage(), e);
        }
    }

    public Optional<ResourcePool> findResourcePoolById(Transaction transaction, LockMode lockMode, UUID id) {
        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.RESOURCES_DB, DatabaseTables.RESOURCE_POOLS_TABLE, id),
                lockMode
        );

        String sql = DatabaseQueries.SELECT_RESOURCE_BY_ID;

        try {
            ResourcePool pool = this.jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(ResourcePool.class),
                    id
            );

            return Optional.ofNullable(pool);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<UUID> findComputeNodeIdsByPool(UUID poolId) {
        try {
            return this.jdbcTemplate.queryForList(
                    DatabaseQueries.SELECT_COMPUTE_NODE_IDS_BY_POOL_ID,
                    UUID.class,
                    poolId
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch compute nodes of pool: " + e.getMessage(), e);
        }
    }

    public List<ResourcePool> findAllResourcePools() {
        String sql = DatabaseQueries.SELECT_RESOURCES;

        try {
            return this.jdbcTemplate.query(
                    sql,
                    new BeanPropertyRowMapper<>(ResourcePool.class)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch resource pools: " + e.getMessage(), e);
        }
    }


    public ComputeNode saveComputeNode(Transaction transaction, ComputeNode node) {
        if (node.getId() == null) {
            node.setId(UUID.randomUUID());
        }
        if (node.getStatus() == null) {
            node.setStatus("active");
        }

        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.RESOURCES_DB, DatabaseTables.COMPUTE_NODES_TABLE, node.getId()),
                LockMode.EXCLUSIVE
        );

        transaction.getUndoLog().push(new UndoEntry(
                DatabaseName.RESOURCES_DB,
                DatabaseQueries.DELETE_COMPUTE_NODE_BY_ID,
                new Object[] { node.getId() }
        ));

        String sql = DatabaseQueries.INSERT_COMPUTE_NODE;

        try {
            this.jdbcTemplate.update(
                    sql,
                    node.getId(),
                    node.getPoolId(),
                    node.getHostname(),
                    node.getCpuCores(),
                    node.getMemoryMb(),
                    node.getAvailableCpuCores(),
                    node.getAvailableMemoryMb(),
                    node.getStatus(),
                    node.getVersion()
            );

            return node;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save compute node: " + e.getMessage(), e);
        }
    }

    public Optional<ComputeNode> findComputeNodeById(Transaction transaction, LockMode lockMode, UUID id) {
        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.RESOURCES_DB, DatabaseTables.COMPUTE_NODES_TABLE, id),
                lockMode
        );

        String sql = DatabaseQueries.SELECT_COMPUTE_NODE_BY_ID;

        try {
            ComputeNode node = this.jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(ComputeNode.class),
                    id
            );
            return Optional.ofNullable(node);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<ComputeNode> findAllComputeNodes() {
        String sql = DatabaseQueries.SELECT_COMPUTE_NODES;

        try {
            return this.jdbcTemplate.query(
                    sql,
                    new BeanPropertyRowMapper<>(ComputeNode.class)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch compute nodes: " + e.getMessage(), e);
        }
    }

    public void updateNodeResources(Transaction transaction, UUID nodeId, int availableCpuCores, long availableMemoryMb) {
        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.RESOURCES_DB, DatabaseTables.COMPUTE_NODES_TABLE, nodeId),
                LockMode.EXCLUSIVE
        );

        Map<String, Object> oldComputeResources = this.jdbcTemplate.queryForMap(
                DatabaseQueries.SELECT_CPU_AND_MEMORY_FROM_COMPUTE_NODE_BY_ID,
                nodeId
        );

        transaction.getUndoLog().push(new UndoEntry(
                DatabaseName.RESOURCES_DB,
                DatabaseQueries.UPDATE_CPU_AND_MEMORY_FOR_COMPUTE_NODE_BY_ID,
                new Object[] { oldComputeResources.get("available_cpu_cores"), oldComputeResources.get("available_memory_mb"), nodeId }
        ));

        try {
            this.jdbcTemplate.update(
                    DatabaseQueries.UPDATE_CPU_AND_MEMORY_FOR_COMPUTE_NODE_BY_ID,
                    availableCpuCores,
                    availableMemoryMb,
                    nodeId
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to update compute node resources: " + e.getMessage(), e);
        }
    }

    public void deleteComputeNode(Transaction transaction, UUID nodeId) {
        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.RESOURCES_DB, DatabaseTables.COMPUTE_NODES_TABLE, nodeId),
                LockMode.EXCLUSIVE
        );

        Map<String, Object> oldNode = this.jdbcTemplate.queryForMap(
                DatabaseQueries.SELECT_COMPUTE_NODE_BY_ID,
                nodeId
        );

        transaction.getUndoLog().push(new UndoEntry(
                DatabaseName.RESOURCES_DB,
                DatabaseQueries.INSERT_COMPUTE_NODE,
                new Object[] {
                        oldNode.get("id"),
                        oldNode.get("pool_id"),
                        oldNode.get("hostname"),
                        oldNode.get("cpu_cores"),
                        oldNode.get("memory_mb"),
                        oldNode.get("available_cpu_cores"),
                        oldNode.get("available_memory_mb"),
                        oldNode.get("status"),
                        oldNode.get("version")
                }
        ));

        try {
            this.jdbcTemplate.update("delete from compute_nodes where id = ?", nodeId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete compute node: " + e.getMessage(), e);
        }
    }

    public void deleteResourcePool(Transaction transaction, UUID poolId) {
        this.lockManager.acquireLock(
                transaction,
                LockManager.resource(DatabaseName.RESOURCES_DB, DatabaseTables.RESOURCE_POOLS_TABLE, poolId),
                LockMode.EXCLUSIVE
        );

        Map<String, Object> oldPool = this.jdbcTemplate.queryForMap(
                DatabaseQueries.SELECT_RESOURCE_BY_ID,
                poolId
        );

        transaction.getUndoLog().push(new UndoEntry(
                DatabaseName.RESOURCES_DB,
                DatabaseQueries.RESTORE_RESOURCE,
                new Object[] {
                        oldPool.get("id"),
                        oldPool.get("name"),
                        oldPool.get("region"),
                        oldPool.get("created_at")
                }
        ));

        try {
            this.jdbcTemplate.update(DatabaseQueries.DELETE_RESOURCE_BY_ID, poolId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete resource pool: " + e.getMessage(), e);
        }
    }
}
