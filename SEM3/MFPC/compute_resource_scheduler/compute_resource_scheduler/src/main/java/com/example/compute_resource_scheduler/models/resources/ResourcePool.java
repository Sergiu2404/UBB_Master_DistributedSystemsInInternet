package com.example.compute_resource_scheduler.models.resources;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ResourcePool {
    private UUID id;
    private String name;
    private String region;
    private OffsetDateTime createdAt;
}
