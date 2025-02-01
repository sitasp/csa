package com.sage.csa.config.ai;

import com.sage.csa.advisor.CaptureMemoryAdvisor;
import com.sage.csa.advisor.SimpleLoggerAdvisor;
import com.sage.csa.service.impl.PgChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder,
                                 CaptureMemoryAdvisor captureMemoryAdvisor,
                                 SimpleLoggerAdvisor loggerAdvisor,
                                 PgChatMemory pgChatMemory){
        return builder
                .defaultAdvisors(new MessageChatMemoryAdvisor(pgChatMemory),
                        loggerAdvisor,
                        captureMemoryAdvisor)
                .build();
    }
}

