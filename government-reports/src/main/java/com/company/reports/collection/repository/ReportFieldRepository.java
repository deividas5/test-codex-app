package com.company.reports.collection.repository;

import com.company.reports.domain.ReportField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportFieldRepository extends JpaRepository<ReportField, Long> {
}
