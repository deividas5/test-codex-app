package com.company.reports.generation.template;

import com.company.reports.domain.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportTemplateService {
    public String render(String reportType, List<ApiResponse> responses) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body><h2>").append(reportType).append("</h2><table border='1'><tr><th>Provider</th><th>XML</th></tr>");
        for (ApiResponse response : responses) {
            sb.append("<tr><td>").append(response.getProviderId()).append("</td><td><pre>")
                    .append(escape(response.getResponseXml())).append("</pre></td></tr>");
        }
        sb.append("</table></body></html>");
        return sb.toString();
    }

    private String escape(String val) {
        return val == null ? "" : val.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
