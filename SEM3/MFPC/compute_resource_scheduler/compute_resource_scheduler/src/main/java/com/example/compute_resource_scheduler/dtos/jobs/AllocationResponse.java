package com.example.compute_resource_scheduler.dtos.jobs;

import com.example.compute_resource_scheduler.models.jobs.Allocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllocationResponse {

    private UUID id;
    private UUID jobId;
    private UUID computeNodeId;

    private int allocatedCpuCores;
    private long allocatedMemoryMb;

    private String status;

    private OffsetDateTime createdAt;
    private OffsetDateTime releasedAt;

    public static AllocationResponse from(Allocation entity) {
        return new AllocationResponse(
                entity.getId(),
                entity.getJobId(),
                entity.getComputeNodeId(),
                entity.getAllocatedCpuCores(),
                entity.getAllocatedMemoryMb(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getReleasedAt()
        );
    }
}