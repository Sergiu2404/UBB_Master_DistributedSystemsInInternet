package com.example.compute_resource_scheduler.service;

import com.example.compute_resource_scheduler.dtos.resources.ComputeNodeResponse;
import com.example.compute_resource_scheduler.dtos.resources.CreateComputeNodeRequest;
import com.example.compute_resource_scheduler.dtos.resources.CreateResourcePoolRequest;
import com.example.compute_resource_scheduler.dtos.resources.ResourcePoolResponse;
import com.example.compute_resource_scheduler.models.resources.ComputeNode;
import com.example.compute_resource_scheduler.models.resources.ResourcePool;
import com.example.compute_resource_scheduler.repository.ResourcesRepository;
import com.example.compute_resource_scheduler.transaction.TransactionManager;
import com.example.compute_resource_scheduler.transaction.enums.LockMode;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResourceService {
    private final ResourcesRepository resourcesRepository;
    private final TransactionManager transactionManager;

    public ResourceService(ResourcesRepository resourcesRepository, TransactionManager transactionManager) {
        this.resourcesRepository = resourcesRepository;
        this.transactionManager = transactionManager;
    }

    public ResourcePoolResponse createResourcePool(CreateResourcePoolRequest request) {
        ResourcePool pool = new ResourcePool();

        pool.setId(UUID.randomUUID());
        pool.setName(request.getName());
        pool.setRegion(request.getRegion());
        pool.setCreatedAt(OffsetDateTime.now());

        return ResourcePoolResponse.from(
                transactionManager.execute(transaction -> resourcesRepository.saveResourcePool(transaction, pool))
        );
    }

    public Optional<ResourcePool> findPoolById(UUID id) {
        return transactionManager.execute(transaction -> resourcesRepository.findResourcePoolById(transaction, LockMode.SHARED, id));
    }

    public List<ResourcePoolResponse> findAllPools() {
        return resourcesRepository.findAllResourcePools().stream()
                .map(ResourcePoolResponse::from)
                .toList();
    }

    public ComputeNodeResponse createComputeNode(CreateComputeNodeRequest request) {
        ComputeNode node = new ComputeNode();

        node.setId(UUID.randomUUID());
        node.setPoolId(request.getPoolId());
        node.setHostname(request.getHostname());
        node.setCpuCores(request.getCpuCores());
        node.setMemoryMb(request.getMemoryMb());

        node.setAvailableCpuCores(request.getCpuCores());
        node.setAvailableMemoryMb(request.getMemoryMb());
        node.setStatus("active");
        node.setVersion(0L);

        return ComputeNodeResponse.from(
                transactionManager.execute(transaction -> {
                    resourcesRepository.findResourcePoolById(transaction, LockMode.SHARED, request.getPoolId()) // block concurrent pool delete until this commits
                            .orElseThrow(() -> new IllegalArgumentException("resource pool not found with id: " + request.getPoolId()));

                    return resourcesRepository.saveComputeNode(transaction, node);
                })
        );
    }

    public Optional<ComputeNode> findNodeById(UUID id) {
        return transactionManager.execute(transaction -> resourcesRepository.findComputeNodeById(transaction, LockMode.SHARED, id));
    }

    public List<ComputeNodeResponse> findAllNodes() {
        return resourcesRepository.findAllComputeNodes().stream()
                .map(ComputeNodeResponse::from)
                .toList();
    }
}
