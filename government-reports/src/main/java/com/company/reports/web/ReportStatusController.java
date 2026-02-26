package com.company.reports.web;

import com.company.reports.collection.service.ReportWorkflowService;
import com.company.reports.generation.service.ReportQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ReportStatusController {
    private final ReportQueryService reportQueryService;
    private final ReportWorkflowService workflowService;

    @GetMapping("/reports/status/{requestId}")
    public String status(@PathVariable Long requestId, Model model) {
        model.addAttribute("request", reportQueryService.getRequest(requestId));
        model.addAttribute("jobs", reportQueryService.getJobs(requestId));
        return "status-page";
    }

    @GetMapping("/reports/status/{requestId}/fragment")
    public String statusFragment(@PathVariable Long requestId, Model model) {
        model.addAttribute("request", reportQueryService.getRequest(requestId));
        model.addAttribute("jobs", reportQueryService.getJobs(requestId));
        return "fragments/status-fragment :: content";
    }

    @PostMapping("/reports/jobs/{jobId}/retry")
    public String retry(@PathVariable Long jobId) {
        var job = reportQueryService.getJob(jobId);
        workflowService.processJob(jobId, java.util.Map.of());
        return "redirect:/reports/status/" + job.getRequest().getId();
    }

    @GetMapping("/reports/jobs/{jobId}/download")
    public ResponseEntity<String> download(@PathVariable Long jobId) {
        var job = reportQueryService.getJob(jobId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + job.getReportType() + "-" + jobId + ".html")
                .contentType(MediaType.TEXT_HTML)
                .body(job.getGeneratedHtml() == null ? "<html><body>Not ready</body></html>" : job.getGeneratedHtml());
    }
}
