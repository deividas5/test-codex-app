package com.company.reports.collection.provider;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class MockRegistryProvider implements GovernmentDataProvider {
    @Override
    public String getProviderId() { return "REGISTRY"; }

    @Override
    public String fetchRawData(Map<String, String> parameters) {
        sleep(2000, 3000);
        return "<registry><properties><property>Office Vilnius</property><property>Warehouse Kaunas</property><property>Land plot Klaipeda</property></properties></registry>";
    }

    private void sleep(int min, int max) {
        try { Thread.sleep(ThreadLocalRandom.current().nextInt(min, max)); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }
}
