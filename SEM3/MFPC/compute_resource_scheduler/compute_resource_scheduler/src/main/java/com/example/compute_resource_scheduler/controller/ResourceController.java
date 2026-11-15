package com.example.compute_resource_scheduler.controller;

import com.example.compute_resource_scheduler.dtos.resources.ComputeNodeResponse;
import com.example.compute_resource_scheduler.dtos.resources.CreateComputeNodeRequest;
import com.example.compute_resource_scheduler.dtos.resources.CreateResourcePoolRequest;
import com.example.compute_resource_scheduler.dtos.resources.ResourcePoolResponse;
import com.example.compute_resource_scheduler.service.JobResourceAllocationService;
import com.example.compute_resource_scheduler.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {
    @Autowired
    private JobResourceAllocationService jobResourceAllocationService;
    @Autowired
    private ResourceService resourceService;

    @PostMapping("/nodes")
    public ResponseEntity<ComputeNodeResponse> createComputeNode(
            @Valid @RequestBody CreateComputeNodeRequest createRequest
            ) {
        ComputeNodeResponse computeNode = this.resourceService.createComputeNode(createRequest);
        return ResponseEntity.ok(computeNode);
    }

    @PostMapping("/pools")
    public ResponseEntity<ResourcePoolResponse> createResourcePool(
            @Valid @RequestBody CreateResourcePoolRequest createRequest
            ) {
        ResourcePoolResponse resourcePool = this.resourceService.createResourcePool(createRequest);
        return ResponseEntity.ok(resourcePool);
    }

    @GetMapping("/pools")
    public ResponseEntity<List<ResourcePoolResponse>> getAllResourcePools() {
        return ResponseEntity.ok(this.resourceService.findAllPools());
    }

    @GetMapping("/nodes")
    public ResponseEntity<List<ComputeNodeResponse>> getAllComputeNodes() {
        return ResponseEntity.ok(this.resourceService.findAllNodes());
    }

    @DeleteMapping("/nodes/{nodeId}")
    public ResponseEntity<List<UUID>> decommissionNode(
            @PathVariable UUID nodeId,
            @RequestParam(defaultValue = "0") long demoDelayMs
            ) {
        return ResponseEntity.ok(this.jobResourceAllocationService.decommissionNode(nodeId, demoDelayMs));
    }

    @DeleteMapping("/pools/{poolId}")
    public ResponseEntity<List<UUID>> deleteResourcePool(
            @PathVariable UUID poolId,
            @RequestParam(defaultValue = "0") long demoDelayMs
    ) {
        return ResponseEntity.ok(this.jobResourceAllocationService.deleteResourcePool(poolId, demoDelayMs));
    }
}
