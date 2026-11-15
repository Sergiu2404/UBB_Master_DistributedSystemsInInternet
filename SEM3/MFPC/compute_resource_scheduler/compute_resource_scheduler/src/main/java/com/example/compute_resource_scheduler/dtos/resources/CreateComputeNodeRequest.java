package com.example.compute_resource_scheduler.dtos.resources;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CreateComputeNodeRequest {
    @NotNull
    private UUID poolId;

    @NotBlank
    @Size(max = 255)
    private String hostname;

    @Min(1)
    private int cpuCores;

    @Min(1)
    private long memoryMb;
}
