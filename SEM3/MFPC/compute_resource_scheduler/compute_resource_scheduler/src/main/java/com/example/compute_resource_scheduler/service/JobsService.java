package com.example.compute_resource_scheduler.service;

import com.example.compute_resource_scheduler.dtos.jobs.AllocationResponse;
import com.example.compute_resource_scheduler.dtos.jobs.CreateJobRequest;
import com.example.compute_resource_scheduler.dtos.jobs.JobResponse;
import com.example.compute_resource_scheduler.models.jobs.Allocation;
import com.example.compute_resource_scheduler.models.jobs.Job;
import com.example.compute_resource_scheduler.repository.JobsRepository;
import com.example.compute_resource_scheduler.transaction.TransactionManager;
import com.example.compute_resource_scheduler.transaction.enums.LockMode;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobsService {
    private final JobsRepository jobsRepository;
    private final TransactionManager transactionManager;

    public JobsService(JobsRepository jobsRepository, TransactionManager transactionManager) {
        this.jobsRepository = jobsRepository;
        this.transactionManager = transactionManager;
    }

    public JobResponse createJob(CreateJobRequest request) {
        Job job = new Job();

        job.setId(UUID.randomUUID());
        job.setName(request.getName());
        job.setRequiredCpuCores(request.getRequiredCpuCores());
        job.setRequiredMemoryMb(request.getRequiredMemoryMb());
        job.setPriority(request.getPriority());
        job.setStatus("pending");
        job.setSubmittedAt(OffsetDateTime.now());

        return JobResponse.from(
                transactionManager.execute(transaction -> jobsRepository.saveJob(transaction, job))
        );
    }

    public Optional<Job> findJobById(UUID id) {
        return transactionManager.execute(transaction -> jobsRepository.findJobById(transaction, LockMode.SHARED, id));
    }

    public List<JobResponse> findAllJobs() {
        List<Job> allJobs = jobsRepository.findAllJobs();

        return allJobs.stream()
                .map(JobResponse::from)
                .toList();
    }

    public List<AllocationResponse> findAllAlocations() {
        List<Allocation> allJobs = jobsRepository.findAllAlocations();

        return allJobs.stream()
                .map(AllocationResponse::from)
                .toList();
    }

    public List<AllocationResponse> findAllocationsByJob(UUID jobId) {
        return jobsRepository.findAllocationsByJob(jobId).stream()
                .map(AllocationResponse::from)
                .toList();
    }

    public List<AllocationResponse> findAllocationsByNode(UUID nodeId) {
        return jobsRepository.findAllocationsByNode(nodeId).stream()
                .map(AllocationResponse::from)
                .toList();
    }
}
