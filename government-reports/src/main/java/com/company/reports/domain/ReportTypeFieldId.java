package com.company.reports.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReportTypeFieldId implements Serializable {
    private Long reportTypeId;
    private Long fieldId;
}
