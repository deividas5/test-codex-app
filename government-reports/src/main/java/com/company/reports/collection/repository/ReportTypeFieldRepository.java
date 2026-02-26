package com.company.reports.collection.repository;

import com.company.reports.domain.ReportTypeField;
import com.company.reports.domain.ReportTypeFieldId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportTypeFieldRepository extends JpaRepository<ReportTypeField, ReportTypeFieldId> {
    List<ReportTypeField> findByReportTypeIdIn(List<Long> reportTypeIds);
}
