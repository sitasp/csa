package com.sage.csa.config.ai;

import com.sage.csa.advisor.CaptureMemoryAdvisor;
import com.sage.csa.service.impl.PgChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder
                .build();
    }

    @Bean
    public List<Advisor> advisorList(CaptureMemoryAdvisor captureMemoryAdvisor,
                                     PgChatMemory pgChatMemory){
        return List.of(new MessageChatMemoryAdvisor(pgChatMemory),
                captureMemoryAdvisor);
    }
}

