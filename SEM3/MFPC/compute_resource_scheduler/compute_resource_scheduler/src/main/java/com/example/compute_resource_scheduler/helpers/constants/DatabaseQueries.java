package com.example.compute_resource_scheduler.helpers.constants;

public class DatabaseQueries {
    public static final String DELETE_RESOURCE_BY_ID = "delete from resource_pools where id = ?";
    public static final String INSERT_RESOURCE = """ 
        insert into resource_pools (id, name, region)
        values (?, ?, ?)
    """;
    public static final String SELECT_RESOURCE_BY_ID = "select * from resource_pools where id = ?";
    public static final String SELECT_RESOURCES = "select * from resource_pools";

    public static final String DELETE_COMPUTE_NODE_BY_ID = "delete from compute_nodes where id = ?";
    public static final String INSERT_COMPUTE_NODE = """
        insert into compute_nodes 
        (id, pool_id, hostname, cpu_cores, memory_mb, available_cpu_cores, available_memory_mb, status, version)
        values (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;
    public static final String SELECT_COMPUTE_NODE_BY_ID = "select * from compute_nodes where id = ?";
    public static final String SELECT_COMPUTE_NODES = "select * from compute_nodes";
    public static final String SELECT_CPU_AND_MEMORY_FROM_COMPUTE_NODE_BY_ID = "select available_cpu_cores, available_memory_mb from compute_nodes where id = ?";
    public static final String UPDATE_CPU_AND_MEMORY_FOR_COMPUTE_NODE_BY_ID = "update compute_nodes set available_cpu_cores = ?, available_memory_mb = ? where id = ?";
    public static final String RESTORE_RESOURCE = """
        insert into resource_pools(id, name, region, created_at)
        values (?, ?, ?, ?);
    """;
    public static final String SELECT_COMPUTE_NODE_IDS_BY_POOL_ID = "select id from compute_nodes WHERE pool_id = ?";


    public static final String DELETE_JOB_BY_ID = "delete from jobs where id = ?";
    public static final String INSERT_JOB = """
        insert into jobs (id, name, required_cpu_cores, required_memory_mb, priority, status)
        values (?, ?, ?, ?, ?, ?)
    """;
    public static final String SELECT_JOB_BY_ID = "select * from jobs where id = ?";
    public static final String SELECT_JOBS = "select * from jobs";
    public static final String SELECT_STATUS_FROM_JOB_BY_ID = "select status from jobs where id = ?";
    public static final String UPDATE_STATUS_FOR_JOB_BY_ID = "update jobs set status = ? where id = ?";


    public static final String SELECT_ALLOCATIONS = "select * from allocations";
    public static final String DELETE_ALLOCATION_BY_ID = "delete from allocations where id = ?";
    public static final String INSERT_ALLOCATION = """
        insert into allocations (id, job_id, compute_node_id, allocated_cpu_cores, allocated_memory_mb, status)
        values (?, ?, ?, ?, ?, ?)
    """;
    public static final String SELECT_ACTIVE_ALLOCATIONS_BY_JOB_ID = "select * from allocations where job_id = ? and status = 'active'";
    public static final String SELECT_ACTIVE_ALLOCATIONS_BY_COMPUTE_NODE_ID = "select * from allocations where compute_node_id = ? and status = 'active'";
    public static final String SELECT_ALLOCATIONS_BY_JOB_ID = "select * from allocations where job_id = ? order by created_at desc";
    public static final String SELECT_ALLOCATIONS_BY_COMPUTE_NODE_ID = "select * from allocations where compute_node_id = ? order by created_at desc";
    public static final String SELECT_STATUS_AND_RELEASED_TIMESTAMP_FROM_ALLOCATION_BY_ID = "select status, released_at from allocations where id = ?";
    public static final String UPDATE_STATUS_AND_RELEASED_TIMESTAMP_FOR_ALLOCATION_BY_ID = "update allocations set status = ?, released_at = ? where id = ?";
    public static final String UPDATE_STATUS_AND_RELEASED_TIMESTAMP_TO_NOW_FOR_ALLOCATION_BY_ID = "update allocations set status = 'released', released_at = now() where id = ?";
}
