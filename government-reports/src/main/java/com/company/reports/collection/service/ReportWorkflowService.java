package com.company.reports.collection.service;

import com.company.reports.collection.provider.GovernmentDataProvider;
import com.company.reports.collection.repository.ApiResponseRepository;
import com.company.reports.collection.repository.ReportJobRepository;
import com.company.reports.collection.repository.ReportRequestRepository;
import com.company.reports.domain.*;
import com.company.reports.generation.template.ReportTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportWorkflowService {
    private final ReportRequestRepository requestRepository;
    private final ReportJobRepository jobRepository;
    private final ApiResponseRepository apiResponseRepository;
    private final List<GovernmentDataProvider> providers;
    private final ReportTemplateService templateService;

    @Transactional
    public Long createRequest(List<String> reportTypes, Map<String, String> fields) {
        ReportRequest request = new ReportRequest();
        request.setUserId("prototype-user");
        request.setCreatedAt(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);
        request.setTotalReports(reportTypes.size());
        request.setCompletedReports(0);
        requestRepository.save(request);

        for (String reportType : reportTypes) {
            ReportJob job = new ReportJob();
            job.setRequest(request);
            job.setReportType(reportType);
            job.setStatus(JobStatus.PENDING);
            job.setCreatedAt(LocalDateTime.now());
            jobRepository.save(job);
            processJob(job.getId(), fields);
        }
        return request.getId();
    }

    @Async
    public void processJob(Long jobId, Map<String, String> fields) {
        ReportJob job = jobRepository.findById(jobId).orElseThrow();
        ReportRequest request = job.getRequest();
        request.setStatus(RequestStatus.COLLECTING);
        job.setStatus(JobStatus.COLLECTING);
        requestRepository.save(request);
        jobRepository.save(job);

        try {
            for (GovernmentDataProvider provider : providers) {
                ApiResponse response = fetchWithRetry(job, provider, fields);
                apiResponseRepository.save(response);
            }

            job.setStatus(JobStatus.GENERATING);
            request.setStatus(RequestStatus.GENERATING);
            jobRepository.save(job);
            requestRepository.save(request);

            String html = templateService.render(job.getReportType(), apiResponseRepository.findByJobId(jobId));
            job.setGeneratedHtml(html);
            job.setStatus(JobStatus.COMPLETED);
            job.setCompletedAt(LocalDateTime.now());
            jobRepository.save(job);

            request.setCompletedReports(request.getCompletedReports() + 1);
            request.setStatus(request.getCompletedReports() == request.getTotalReports() ? RequestStatus.COMPLETED : RequestStatus.READY);
            requestRepository.save(request);
        } catch (Exception ex) {
            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(ex.getMessage());
            jobRepository.save(job);
            request.setStatus(RequestStatus.FAILED);
            requestRepository.save(request);
        }
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 400, multiplier = 2))
    public ApiResponse fetchWithRetry(ReportJob job, GovernmentDataProvider provider, Map<String, String> fields) {
        String xml = provider.fetchRawData(fields);
        ApiResponse response = new ApiResponse();
        response.setJob(job);
        response.setProviderId(provider.getProviderId());
        response.setCollectedAt(LocalDateTime.now());
        response.setResponseXml(xml);
        response.setStatus(ProviderStatus.SUCCESS);
        return response;
    }

    @Recover
    public ApiResponse recover(Exception ex, ReportJob job, GovernmentDataProvider provider, Map<String, String> fields) {
        ApiResponse response = new ApiResponse();
        response.setJob(job);
        response.setProviderId(provider.getProviderId());
        response.setCollectedAt(LocalDateTime.now());
        response.setResponseXml("<error>" + ex.getMessage() + "</error>");
        response.setStatus(ProviderStatus.FAILED);
        return response;
    }
}
