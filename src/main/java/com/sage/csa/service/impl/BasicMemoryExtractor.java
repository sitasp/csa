package com.sage.csa.service.impl;

import com.sage.csa.service.MemoryExtractor;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.stereotype.Component;

@Component
public class BasicMemoryExtractor implements MemoryExtractor {
    @Override
    public String extractRequestContext(AdvisedRequest request) {
        return request.userText();
    }

    @Override
    public String extractResponseContext(AdvisedResponse response) {
        return response.response().getResult().getOutput().getContent();
    }
}
