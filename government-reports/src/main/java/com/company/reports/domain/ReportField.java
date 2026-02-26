package com.company.reports.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "report_fields")
public class ReportField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Long id;

    private String fieldName;
    private String fieldLabel;
    private String fieldType;
    private String validationRules;
    private String helpText;
}
