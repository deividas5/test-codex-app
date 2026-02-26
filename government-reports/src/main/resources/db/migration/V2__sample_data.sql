INSERT INTO report_types(report_name, description, category) VALUES
('COMPANY_REPORT', 'Company profile and registry data', 'COMPANY'),
('TAX_REPORT', 'Tax and social contribution summary', 'FINANCE');

INSERT INTO report_fields(field_name, field_label, field_type, validation_rules, help_text) VALUES
('company_code', 'Company Code', 'text', 'required', '9 digit legal entity code'),
('report_period_start', 'Report period start', 'date', 'required', 'Start date'),
('report_period_end', 'Report period end', 'date', 'required', 'End date'),
('tax_type', 'Tax Type', 'select', 'required', 'Type of tax declaration');

INSERT INTO report_type_fields(report_type_id, field_id, is_required, display_order) VALUES
(1, 1, true, 1),
(1, 2, true, 2),
(1, 3, true, 3),
(2, 1, true, 1),
(2, 2, true, 2),
(2, 3, true, 3),
(2, 4, true, 4);

INSERT INTO field_options(field_id, option_value, option_label, display_order) VALUES
(4, 'VAT', 'VAT', 1),
(4, 'CIT', 'Corporate Income Tax', 2),
(4, 'PIT', 'Personal Income Tax', 3);
