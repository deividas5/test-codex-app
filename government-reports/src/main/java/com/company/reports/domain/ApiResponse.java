package com.company.reports.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "api_responses")
public class ApiResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private ReportJob job;

    private String providerId;

    @Lob
    private String responseXml;

    private LocalDateTime collectedAt;

    @Enumerated(EnumType.STRING)
    private ProviderStatus status;
}
