package com.example.compute_resource_scheduler.dtos.jobs;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CreateAllocationRequest {
    @NotNull
    private UUID jobId;

    @NotNull
    private UUID computeNodeId;
}