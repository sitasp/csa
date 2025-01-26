package com.sage.csa.dto.objects;

import lombok.Data;

@Data
public class MemoryLLMResponse {
    String content;
    boolean isUseful;
}
