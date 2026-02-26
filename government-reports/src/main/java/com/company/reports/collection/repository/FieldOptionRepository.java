package com.company.reports.collection.repository;

import com.company.reports.domain.FieldOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldOptionRepository extends JpaRepository<FieldOption, Long> {
    List<FieldOption> findByFieldIdOrderByDisplayOrderAsc(Long fieldId);
}
