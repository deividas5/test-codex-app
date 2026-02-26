package com.company.reports.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "report_type_fields")
@IdClass(ReportTypeFieldId.class)
public class ReportTypeField {
    @Id
    @Column(name = "report_type_id")
    private Long reportTypeId;

    @Id
    @Column(name = "field_id")
    private Long fieldId;

    private boolean isRequired;
    private int displayOrder;
}
