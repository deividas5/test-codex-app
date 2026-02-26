package com.company.reports.collection.service;

import com.company.reports.collection.repository.*;
import com.company.reports.domain.DynamicFieldView;
import com.company.reports.domain.ReportField;
import com.company.reports.domain.ReportType;
import com.company.reports.domain.ReportTypeField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FieldConfigurationService {
    private final ReportTypeRepository reportTypeRepository;
    private final ReportTypeFieldRepository reportTypeFieldRepository;
    private final ReportFieldRepository reportFieldRepository;
    private final FieldOptionRepository fieldOptionRepository;

    public List<ReportType> getAllReportTypes() {
        return reportTypeRepository.findAll();
    }

    public List<DynamicFieldView> resolveFields(List<String> reportTypeNames) {
        if (reportTypeNames == null || reportTypeNames.isEmpty()) return List.of();
        List<ReportType> types = reportTypeRepository.findByReportNameIn(reportTypeNames);
        List<Long> ids = types.stream().map(ReportType::getId).toList();
        List<ReportTypeField> links = reportTypeFieldRepository.findByReportTypeIdIn(ids);
        Map<Long, ReportField> allFields = reportFieldRepository.findAll().stream().collect(java.util.stream.Collectors.toMap(ReportField::getId, x -> x));
        Map<Long, DynamicFieldView> merged = new HashMap<>();
        links.stream().sorted(Comparator.comparingInt(ReportTypeField::getDisplayOrder)).forEach(link -> {
            ReportField f = allFields.get(link.getFieldId());
            if (f != null) {
                merged.compute(f.getId(), (k, existing) -> DynamicFieldView.builder()
                        .id(f.getId()).name(f.getFieldName()).label(f.getFieldLabel()).type(f.getFieldType())
                        .helpText(f.getHelpText()).required((existing != null && existing.isRequired()) || link.isRequired())
                        .options(fieldOptionRepository.findByFieldIdOrderByDisplayOrderAsc(f.getId()))
                        .build());
            }
        });
        return new ArrayList<>(merged.values());
    }
}
