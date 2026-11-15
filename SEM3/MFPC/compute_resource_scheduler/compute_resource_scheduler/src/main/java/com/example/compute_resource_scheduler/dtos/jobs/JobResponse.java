package com.example.compute_resource_scheduler.dtos.jobs;

import com.example.compute_resource_scheduler.models.jobs.Job;
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
public class JobResponse {
    private UUID id;
    private String name;

    private int requiredCpuCores;
    private long requiredMemoryMb;

    private int priority;
    private String status;

    private OffsetDateTime submittedAt;

    public static JobResponse from(Job entity) {
        return new JobResponse(
                entity.getId(),
                entity.getName(),
                entity.getRequiredCpuCores(),
                entity.getRequiredMemoryMb(),
                entity.getPriority(),
                entity.getStatus(),
                entity.getSubmittedAt()
        );
    }
}
