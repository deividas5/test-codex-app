# Government Reporting Prototype (Spring Boot 3.5+)

## Run
```bash
mvn spring-boot:run
```

## URLs
- App: http://localhost:8080/reports/request
- Status page: `/reports/status/{requestId}` after submit
- H2 console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/reports;AUTO_SERVER=TRUE`
  - user: `sa`

## Workflow test
1. Open request page.
2. Select `COMPANY_REPORT` and/or `TAX_REPORT`.
3. Dynamic fields load via HTMX (`/api/report-fields`).
4. Submit form.
5. Watch polling status updates every 2 seconds.
6. Download completed HTML reports.

## Database schema diagram
```text
REPORT_REQUESTS 1 --- * REPORT_JOBS 1 --- * API_RESPONSES
REPORT_TYPES * --- * REPORT_FIELDS (through REPORT_TYPE_FIELDS)
REPORT_FIELDS 1 --- * FIELD_OPTIONS
```

## Sample request values
- company_code: `123456789`
- report_period_start: `2024-01-01`
- report_period_end: `2024-12-31`
- tax_type: `VAT`

## Notes
- Async processing uses `@Async`.
- Retry logic uses Spring Retry (3 attempts, exponential backoff).
- Mock providers: VMI, SODRA, Registry.
