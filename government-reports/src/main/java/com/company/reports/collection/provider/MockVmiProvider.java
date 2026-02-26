package com.company.reports.collection.provider;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class MockVmiProvider implements GovernmentDataProvider {
    @Override
    public String getProviderId() { return "VMI"; }

    @Override
    public String fetchRawData(Map<String, String> parameters) {
        sleep(2000, 3000);
        if (ThreadLocalRandom.current().nextInt(10) == 0) throw new IllegalStateException("VMI temporary failure");
        String companyCode = parameters.getOrDefault("company_code", "000000000");
        return "<vmi><company><name>UAB Prototype</name><code>" + companyCode + "</code><registrationDate>" + LocalDate.now().minusYears(5) + "</registrationDate><address>Vilnius, LT</address></company></vmi>";
    }

    private void sleep(int min, int max) {
        try { Thread.sleep(ThreadLocalRandom.current().nextInt(min, max)); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }
}
