package com.company.reports.collection.repository;

import com.company.reports.domain.ReportRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRequestRepository extends JpaRepository<ReportRequest, Long> {
}
