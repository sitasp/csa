package com.sage.csa.dto.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sage.csa.dto.response.ChatSessionResponse;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatHistoryResponse {
    List<ChatMessageResponse> chatMessages;
    ChatSessionResponse chatSession;
}
