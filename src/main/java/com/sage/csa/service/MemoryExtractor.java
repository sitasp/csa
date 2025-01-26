package com.sage.csa.service;

import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;

public interface MemoryExtractor {
    String extractRequestContext(AdvisedRequest request);
    String extractResponseContext(AdvisedResponse response);
}
