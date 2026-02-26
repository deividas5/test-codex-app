package com.company.reports.web;

import com.company.reports.collection.service.FieldConfigurationService;
import com.company.reports.collection.service.ReportWorkflowService;
import com.company.reports.domain.DynamicFieldView;
import com.company.reports.domain.ReportRequestForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ReportRequestController {
    private final FieldConfigurationService fieldConfigurationService;
    private final ReportWorkflowService reportWorkflowService;

    @GetMapping("/reports/request")
    public String requestForm(Model model) {
        model.addAttribute("reportTypes", fieldConfigurationService.getAllReportTypes());
        model.addAttribute("requestForm", new ReportRequestForm());
        return "request-form";
    }

    @GetMapping("/api/report-fields")
    public String dynamicFields(@RequestParam(name = "reportTypes", required = false) List<String> reportTypes, Model model) {
        List<DynamicFieldView> fields = fieldConfigurationService.resolveFields(reportTypes);
        model.addAttribute("fields", fields);
        return "fragments/field-fragment :: fields";
    }

    @PostMapping("/reports/request")
    public String submit(@RequestParam MultiValueMap<String, String> params, RedirectAttributes redirectAttributes) {
        List<String> reportTypes = params.get("reportTypes");
        if (reportTypes == null || reportTypes.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Select at least one report type");
            return "redirect:/reports/request";
        }
        Map<String, String> fieldMap = new HashMap<>();
        params.forEach((k, v) -> {
            if (!"reportTypes".equals(k) && !v.isEmpty()) fieldMap.put(k, v.getFirst());
        });
        Long requestId = reportWorkflowService.createRequest(reportTypes, fieldMap);
        return "redirect:/reports/status/" + requestId;
    }
}
