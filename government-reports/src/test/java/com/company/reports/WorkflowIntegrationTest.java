package com.company.reports;

import com.company.reports.collection.service.ReportWorkflowService;
import com.company.reports.generation.service.ReportQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WorkflowIntegrationTest {
    @Autowired
    private ReportWorkflowService workflowService;
    @Autowired
    private ReportQueryService queryService;

    @Test
    void createsRequestAndJobs() {
        Long requestId = workflowService.createRequest(List.of("COMPANY_REPORT"), Map.of("company_code", "123456789"));
        assertThat(queryService.getRequest(requestId).getTotalReports()).isEqualTo(1);
    }
}
