package com.example.compute_resource_scheduler.dtos.resources;

import com.example.compute_resource_scheduler.models.resources.ResourcePool;
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
public class ResourcePoolResponse {

    private UUID id;
    private String name;
    private String region;
    private OffsetDateTime createdAt;

    public static ResourcePoolResponse from(ResourcePool entity) {
        return new ResourcePoolResponse(
                entity.getId(),
                entity.getName(),
                entity.getRegion(),
                entity.getCreatedAt()
        );
    }
}
