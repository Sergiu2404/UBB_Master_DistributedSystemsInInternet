package com.example.compute_resource_scheduler.models.resources;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ComputeNode {
    private UUID id;
    private UUID poolId;
    private String hostname;

    private int cpuCores;
    private long memoryMb;

    private int availableCpuCores;
    private long availableMemoryMb;

    private String status;
    private long version;
}
