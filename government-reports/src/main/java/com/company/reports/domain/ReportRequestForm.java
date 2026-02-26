package com.company.reports.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ReportRequestForm {
    private List<String> reportTypes = new ArrayList<>();
    private Map<String, String> fieldValues = new HashMap<>();
}
