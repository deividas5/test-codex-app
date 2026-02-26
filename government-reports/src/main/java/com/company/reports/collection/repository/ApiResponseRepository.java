package com.company.reports.collection.repository;

import com.company.reports.domain.ApiResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiResponseRepository extends JpaRepository<ApiResponse, Long> {
    List<ApiResponse> findByJobId(Long jobId);
}
