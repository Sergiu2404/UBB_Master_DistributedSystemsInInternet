package com.example.compute_resource_scheduler.models.jobs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Allocation {
    private UUID id;
    private UUID jobId;
    private UUID computeNodeId;

    private int allocatedCpuCores;
    private long allocatedMemoryMb;

    private String status;

    private OffsetDateTime createdAt;
    private OffsetDateTime releasedAt;
}
