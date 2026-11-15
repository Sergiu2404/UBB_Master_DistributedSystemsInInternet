package com.example.compute_resource_scheduler.controller;

import com.example.compute_resource_scheduler.dtos.jobs.AllocationResponse;
import com.example.compute_resource_scheduler.dtos.jobs.CreateJobRequest;
import com.example.compute_resource_scheduler.dtos.jobs.JobResponse;
import com.example.compute_resource_scheduler.service.JobResourceAllocationService;
import com.example.compute_resource_scheduler.service.JobsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class JobController {
    @Autowired
    private JobsService jobsService;
    @Autowired
    private JobResourceAllocationService jobResourceAllocationService;

    @PostMapping("/jobs")
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody CreateJobRequest createRequest
            ) {
        JobResponse job = this.jobsService.createJob(createRequest);
        return ResponseEntity.ok(job);
    }

    @PostMapping("/jobs/{jobId}/allocate/{nodeId}")
    public ResponseEntity<Boolean> allocateJobToNode(
            @PathVariable UUID jobId,
            @PathVariable UUID nodeId
            ) {
        return ResponseEntity.ok(this.jobResourceAllocationService.allocateJobToNode(jobId, nodeId));
    }

    @PostMapping("/jobs/{jobId}/release")
    public ResponseEntity<Boolean> releaseJob(
            @PathVariable UUID jobId,
            @RequestParam(defaultValue = "0") long demoDelayMs
            ) {
        return ResponseEntity.ok(this.jobResourceAllocationService.releaseJobFromNode(jobId, demoDelayMs));
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(this.jobsService.findAllJobs());
    }

    @GetMapping("/allocations")
    public ResponseEntity<List<AllocationResponse>> getAllocations() {
        return ResponseEntity.ok(this.jobsService.findAllAlocations());
    }

    @GetMapping("/allocations/jobs/{jobId}")
    public ResponseEntity<List<AllocationResponse>> getAllocationsByJob(
            @PathVariable UUID jobId
            ) {
        return ResponseEntity.ok(this.jobsService.findAllocationsByJob(jobId));
    }

    @GetMapping("/allocations/nodes/{nodeId}")
    public ResponseEntity<List<AllocationResponse>> getAllocationsByNode(
            @PathVariable UUID nodeId
            ) {
        return ResponseEntity.ok(this.jobsService.findAllocationsByNode(nodeId));
    }
}
