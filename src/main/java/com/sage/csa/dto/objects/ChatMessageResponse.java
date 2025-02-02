package com.sage.csa.dto.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.ai.chat.messages.MessageType;

import java.time.Instant;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessageResponse {
    private String message;
    private MessageType messageType;
    private Instant createdAt;
}
