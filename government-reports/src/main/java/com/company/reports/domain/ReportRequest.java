package com.company.reports.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "report_requests")
public class ReportRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    private String userId;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private int totalReports;
    private int completedReports;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<ReportJob> jobs = new ArrayList<>();
}
