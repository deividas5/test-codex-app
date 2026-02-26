package com.company.reports.collection.repository;

import com.company.reports.domain.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportTypeRepository extends JpaRepository<ReportType, Long> {
    List<ReportType> findByReportNameIn(List<String> reportNames);
}
