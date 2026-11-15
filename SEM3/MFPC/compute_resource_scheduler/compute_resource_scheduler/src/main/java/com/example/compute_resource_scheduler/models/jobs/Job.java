package com.example.compute_resource_scheduler.models.jobs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Job {
    private UUID id;
    private String name;

    private int requiredCpuCores;
    private long requiredMemoryMb;

    private int priority;
    private String status;

    private OffsetDateTime submittedAt;
}
