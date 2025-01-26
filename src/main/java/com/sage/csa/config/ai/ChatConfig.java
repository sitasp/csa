package com.sage.csa.config.ai;

import com.sage.csa.advisor.CaptureMemoryAdvisor;
import com.sage.csa.service.impl.PgChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder,
                                 CaptureMemoryAdvisor captureMemoryAdvisor,
                                 PgChatMemory pgChatMemory){
        return builder
                .defaultAdvisors(new MessageChatMemoryAdvisor(pgChatMemory), captureMemoryAdvisor)
                .build();
    }
}
