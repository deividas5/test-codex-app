package com.company.reports.collection.provider;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class MockSodraProvider implements GovernmentDataProvider {
    @Override
    public String getProviderId() { return "SODRA"; }

    @Override
    public String fetchRawData(Map<String, String> parameters) {
        sleep(1000, 2000);
        if (ThreadLocalRandom.current().nextInt(20) == 0) throw new IllegalStateException("SODRA temporary failure");
        int employees = ThreadLocalRandom.current().nextInt(12, 120);
        int avgSalary = ThreadLocalRandom.current().nextInt(1500, 4200);
        return "<sodra><stats><employeeCount>" + employees + "</employeeCount><averageSalary>" + avgSalary + "</averageSalary></stats></sodra>";
    }

    private void sleep(int min, int max) {
        try { Thread.sleep(ThreadLocalRandom.current().nextInt(min, max)); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }
}
