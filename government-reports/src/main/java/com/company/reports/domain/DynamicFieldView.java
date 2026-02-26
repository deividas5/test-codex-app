package com.company.reports.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DynamicFieldView {
    Long id;
    String name;
    String label;
    String type;
    boolean required;
    String helpText;
    List<FieldOption> options;
}
