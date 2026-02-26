CREATE TABLE report_requests (
    request_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(100),
    created_at TIMESTAMP,
    status VARCHAR(20),
    total_reports INT,
    completed_reports INT
);

CREATE TABLE report_jobs (
    job_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id BIGINT NOT NULL,
    report_type VARCHAR(100),
    status VARCHAR(20),
    created_at TIMESTAMP,
    completed_at TIMESTAMP,
    error_message VARCHAR(2000),
    generated_html CLOB,
    CONSTRAINT fk_report_job_request FOREIGN KEY (request_id) REFERENCES report_requests(request_id)
);

CREATE TABLE api_responses (
    response_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    provider_id VARCHAR(50),
    response_xml CLOB,
    collected_at TIMESTAMP,
    status VARCHAR(20),
    CONSTRAINT fk_response_job FOREIGN KEY (job_id) REFERENCES report_jobs(job_id)
);

CREATE TABLE report_types (
    report_type_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_name VARCHAR(100),
    description VARCHAR(500),
    category VARCHAR(100)
);

CREATE TABLE report_fields (
    field_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    field_name VARCHAR(100),
    field_label VARCHAR(200),
    field_type VARCHAR(20),
    validation_rules VARCHAR(200),
    help_text VARCHAR(500)
);

CREATE TABLE report_type_fields (
    report_type_id BIGINT,
    field_id BIGINT,
    is_required BOOLEAN,
    display_order INT,
    PRIMARY KEY (report_type_id, field_id),
    CONSTRAINT fk_rtf_type FOREIGN KEY (report_type_id) REFERENCES report_types(report_type_id),
    CONSTRAINT fk_rtf_field FOREIGN KEY (field_id) REFERENCES report_fields(field_id)
);

CREATE TABLE field_options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    field_id BIGINT,
    option_value VARCHAR(100),
    option_label VARCHAR(200),
    display_order INT,
    CONSTRAINT fk_option_field FOREIGN KEY (field_id) REFERENCES report_fields(field_id)
);
