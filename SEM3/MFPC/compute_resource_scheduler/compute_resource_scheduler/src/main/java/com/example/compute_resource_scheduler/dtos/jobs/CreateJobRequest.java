package com.example.compute_resource_scheduler.dtos.jobs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateJobRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    @Min(1)
    private int requiredCpuCores;

    @Min(1)
    private long requiredMemoryMb;

    private int priority;
}
