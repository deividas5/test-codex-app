package com.company.reports;

import com.company.reports.collection.service.FieldConfigurationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FieldConfigurationServiceTest {
    @Autowired
    private FieldConfigurationService service;

    @Test
    void resolvesMergedFields() {
        var fields = service.resolveFields(List.of("COMPANY_REPORT", "TAX_REPORT"));
        assertThat(fields).extracting("name").contains("company_code", "tax_type", "report_period_start");
    }
}
