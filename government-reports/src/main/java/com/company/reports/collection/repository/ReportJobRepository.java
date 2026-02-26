package com.company.reports.collection.repository;

import com.company.reports.domain.ReportJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportJobRepository extends JpaRepository<ReportJob, Long> {
    List<ReportJob> findByRequestId(Long requestId);
}
