package com.company.reports.generation.service;

import com.company.reports.collection.repository.ReportJobRepository;
import com.company.reports.collection.repository.ReportRequestRepository;
import com.company.reports.domain.ReportJob;
import com.company.reports.domain.ReportRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportQueryService {
    private final ReportRequestRepository requestRepository;
    private final ReportJobRepository jobRepository;

    public ReportRequest getRequest(Long requestId) { return requestRepository.findById(requestId).orElseThrow(); }
    public List<ReportJob> getJobs(Long requestId) { return jobRepository.findByRequestId(requestId); }
    public ReportJob getJob(Long jobId) { return jobRepository.findById(jobId).orElseThrow(); }
}
