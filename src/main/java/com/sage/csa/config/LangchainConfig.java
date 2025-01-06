//package com.sage.csa.config;
//
//import dev.langchain4j.data.document.splitter.DocumentSplitters;
//import dev.langchain4j.model.chat.ChatLanguageModel;
//import dev.langchain4j.model.chat.StreamingChatLanguageModel;
//import dev.langchain4j.model.embedding.EmbeddingModel;
//import dev.langchain4j.model.ollama.OllamaChatModel;
//import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
//import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
//import dev.langchain4j.store.embedding.EmbeddingStore;
//import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
//import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class LangchainConfig {
//    @Value("${langchain4j.ollama.chat-model.model-name}")
//    private String modelName;
//
//    @Value("${langchain4j.ollama.chat-model.base-url}")
//    private String baseUrl;
//
//    @Bean
//    StreamingChatLanguageModel streamingChatLanguageModel(){
//        return OllamaStreamingChatModel.builder()
//                .modelName(modelName)
//                .baseUrl(baseUrl)
//                .build();
//    }
//}
