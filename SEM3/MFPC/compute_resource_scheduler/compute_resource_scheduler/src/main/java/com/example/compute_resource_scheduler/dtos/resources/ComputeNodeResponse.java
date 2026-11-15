package com.example.compute_resource_scheduler.dtos.resources;

import com.example.compute_resource_scheduler.models.resources.ComputeNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComputeNodeResponse {
    private UUID id;
    private UUID poolId;
    private String hostname;

    private int cpuCores;
    private long memoryMb;

    private int availableCpuCores;
    private long availableMemoryMb;

    private String status;
    private long version;

    public static ComputeNodeResponse from(ComputeNode entity) {
        return new ComputeNodeResponse(
                entity.getId(),
                entity.getPoolId(),
                entity.getHostname(),
                entity.getCpuCores(),
                entity.getMemoryMb(),
                entity.getAvailableCpuCores(),
                entity.getAvailableMemoryMb(),
                entity.getStatus(),
                entity.getVersion()
        );
    }
}
