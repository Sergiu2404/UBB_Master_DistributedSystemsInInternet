package com.example.compute_resource_scheduler.service;

import com.example.compute_resource_scheduler.models.jobs.Allocation;
import com.example.compute_resource_scheduler.models.jobs.Job;
import com.example.compute_resource_scheduler.models.resources.ComputeNode;
import com.example.compute_resource_scheduler.repository.JobsRepository;
import com.example.compute_resource_scheduler.repository.ResourcesRepository;
import com.example.compute_resource_scheduler.transaction.Transaction;
import com.example.compute_resource_scheduler.transaction.TransactionManager;
import com.example.compute_resource_scheduler.transaction.enums.LockMode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class JobResourceAllocationService {
    private final JobsRepository jobsRepository;
    private final ResourcesRepository resourcesRepository;
    private final TransactionManager transactionManager;

    public JobResourceAllocationService(JobsRepository jobsRepository, ResourcesRepository resourcesRepository, TransactionManager transactionManager) {
        this.jobsRepository = jobsRepository;
        this.resourcesRepository = resourcesRepository;
        this.transactionManager = transactionManager;
    }

    public boolean allocateJobToNode(UUID jobId, UUID nodeId) {
        return this.transactionManager.execute(transaction -> {
            Job job = this.jobsRepository.findJobById(transaction, LockMode.EXCLUSIVE, jobId)
                    .orElseThrow(() -> new IllegalArgumentException("job not found with ID: " + jobId));

            if (!"pending".equals(job.getStatus())) {
                throw new IllegalStateException("job is not in pending state.");
            }

            ComputeNode node = this.resourcesRepository.findComputeNodeById(transaction, LockMode.EXCLUSIVE, nodeId)
                    .orElseThrow(() -> new IllegalArgumentException("compute node not found with id: " + nodeId));

            if (!"active".equals(node.getStatus())) {
                throw new IllegalStateException("compute node is not active");
            }

            if (node.getAvailableCpuCores() < job.getRequiredCpuCores() ||
                    node.getAvailableMemoryMb() < job.getRequiredMemoryMb()) {
                throw new IllegalStateException("Not suficient resources on node " + node.getHostname());
            }

            int newAvailableCpu = node.getAvailableCpuCores() - job.getRequiredCpuCores();
            long newAvailableMemory = node.getAvailableMemoryMb() - job.getRequiredMemoryMb();

            this.resourcesRepository.updateNodeResources(transaction, nodeId, newAvailableCpu, newAvailableMemory);

            Allocation allocation = new Allocation();

            allocation.setId(UUID.randomUUID());
            allocation.setJobId(jobId);
            allocation.setComputeNodeId(nodeId);
            allocation.setAllocatedCpuCores(job.getRequiredCpuCores());
            allocation.setAllocatedMemoryMb(job.getRequiredMemoryMb());
            allocation.setStatus("active");

            this.jobsRepository.insertAllocation(transaction, allocation);
            this.jobsRepository.updateJobStatus(transaction, jobId, "allocated");

            return true;
        });
    }

    public boolean releaseJobFromNode(UUID jobId, long demoDelayMs) {
        return this.transactionManager.execute(transaction -> {
            Job job = this.jobsRepository.findJobById(transaction, LockMode.EXCLUSIVE, jobId)
                    .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));

            if (!"allocated".equals(job.getStatus()) && !"running".equals(job.getStatus())) {
                throw new IllegalStateException("job not allocated to any node");
            }

            Allocation allocation = this.jobsRepository.findActiveAlocationByJob(transaction, jobId)
                    .orElseThrow(() -> new IllegalStateException("no active allocation for job " + jobId));

            pause(demoDelayMs);

            UUID nodeId = allocation.getComputeNodeId();
            ComputeNode node = this.resourcesRepository.findComputeNodeById(transaction, LockMode.EXCLUSIVE, nodeId)
                    .orElseThrow(() -> new IllegalStateException("Compute node not found with ID: " + nodeId));

            int restoredCpu = Math.min(node.getCpuCores(), node.getAvailableCpuCores() + allocation.getAllocatedCpuCores());
            long restoredMemory = Math.min(node.getMemoryMb(), node.getAvailableMemoryMb() + allocation.getAllocatedMemoryMb());

            this.resourcesRepository.updateNodeResources(transaction, nodeId, restoredCpu, restoredMemory);
            this.jobsRepository.releaseAlocation(transaction, allocation.getId());
            this.jobsRepository.updateJobStatus(transaction, jobId, "completed");

            return true;
        });
    }

    public List<UUID> decommissionNode(UUID nodeId, long demoDelayMs) {
        return this.transactionManager.execute(transaction -> {
            this.resourcesRepository.findComputeNodeById(transaction, LockMode.EXCLUSIVE, nodeId)
                    .orElseThrow(() -> new IllegalArgumentException("Computw node not found with ID: " + nodeId));

            pause(demoDelayMs);

            return decommissionLockedNode(transaction, nodeId);
        });
    }

    public List<UUID> deleteResourcePool(UUID poolId, long demoDelayMs) {
        return this.transactionManager.execute(transaction -> {
            this.resourcesRepository.findResourcePoolById(transaction, LockMode.EXCLUSIVE, poolId)
                    .orElseThrow(() -> new IllegalArgumentException("resource pool found with id: " + poolId));

            pause(demoDelayMs);

            List<UUID> requeuedJobIds = new ArrayList<>();

            List<UUID> computeNodeIdsByPool = this.resourcesRepository.findComputeNodeIdsByPool(poolId);

            for (UUID nodeId : computeNodeIdsByPool) {
                this.resourcesRepository.findComputeNodeById(transaction, LockMode.EXCLUSIVE, nodeId)
                        .orElseThrow(() -> new IllegalStateException("Computw node not found with ID: " + nodeId));

                requeuedJobIds.addAll(decommissionLockedNode(transaction, nodeId));
            }

            this.resourcesRepository.deleteResourcePool(transaction, poolId);

            return requeuedJobIds;
        });
    }

    private List<UUID> decommissionLockedNode(Transaction transaction, UUID nodeId) {
        List<Allocation> activeAllocationsByNode = this.jobsRepository.findActiveAlocationsByNode(transaction, nodeId);
        List<UUID> requeuedJobIds = new ArrayList<>();

        for (Allocation activeAllocationByNode : activeAllocationsByNode) {
            this.jobsRepository.findJobById(transaction, LockMode.EXCLUSIVE, activeAllocationByNode.getJobId())
                    .orElseThrow(() -> new IllegalStateException("job not found with ID: " + activeAllocationByNode.getJobId()));

            this.jobsRepository.releaseAlocation(transaction, activeAllocationByNode.getId());
            this.jobsRepository.updateJobStatus(transaction, activeAllocationByNode.getJobId(), "pending");

            requeuedJobIds.add(activeAllocationByNode.getJobId());
        }

        this.resourcesRepository.deleteComputeNode(transaction, nodeId);

        return requeuedJobIds;
    }

    private void pause(long demoDelayMs) {
        if (demoDelayMs <= 0) {
            return;
        }

        try {
            Thread.sleep(demoDelayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
