package com.company.reports.collection.provider;

import java.util.Map;

public interface GovernmentDataProvider {
    String getProviderId();
    String fetchRawData(Map<String, String> parameters);
}
