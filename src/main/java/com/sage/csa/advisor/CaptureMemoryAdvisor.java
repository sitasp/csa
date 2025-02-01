package com.sage.csa.advisor;

import com.sage.csa.dto.objects.MemoryLLMResponse;
import com.sage.csa.service.UserService;
import com.sage.csa.service.impl.BasicMemoryExtractor;
import com.sage.csa.service.impl.PgChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.Content;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@Component
@Slf4j
public class CaptureMemoryAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final ExecutorService executorService;
    private final RetryTemplate retryTemplate;
    private final PgChatMemory pgChatMemory;
    private final UserService userService;
    private final BasicMemoryExtractor basicMemoryExtractor;

    public CaptureMemoryAdvisor(@Value("classpath:prompts/capture-memory.sh") Resource captureMemoryPrompt,
                                @Qualifier("ollamaChatModel") ChatModel chatModel,
                                VectorStore vectorStore,
                                RetryTemplate retryTemplate,
                                PgChatMemory pgChatMemory,
                                UserService userService,
                                BasicMemoryExtractor basicMemoryExtractor) {
        this.chatClient = ChatClient
                .builder(chatModel)
                .defaultSystem(captureMemoryPrompt)
                .build();
        this.vectorStore = vectorStore;
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
        this.retryTemplate = retryTemplate;
        this.pgChatMemory = pgChatMemory;
        this.userService = userService;
        this.basicMemoryExtractor = basicMemoryExtractor;
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest,
                                      CallAroundAdvisorChain chain) {
        log.info("Inside CaptureMemoryAdvisor aroundCall");
        var response = chain.nextAroundCall(advisedRequest);
        SecurityContext context = SecurityContextHolder.getContext();
        executorService.submit(backgroundTask(advisedRequest, response, context));
        return response;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        log.info("Inside CaptureMemoryAdvisor aroundStream");

        SecurityContext context = SecurityContextHolder.getContext();

        return chain.nextAroundStream(advisedRequest)
                .collectList()  // Aggregate all responses into a List
                .doOnSuccess(responses -> {
                    if (!responses.isEmpty()) {
                        executorService.submit(backgroundTask(advisedRequest, responses.get(responses.size() - 1), context));
                    }
                })
                .flatMapMany(Flux::fromIterable); // Convert back to Flux
    }

    private Runnable backgroundTask(AdvisedRequest advisedRequest,
                                    AdvisedResponse response,
                                    SecurityContext context) {
        return () -> {
            try {
                // Set the captured context in the virtual thread
                SecurityContextHolder.setContext(context);

                retryTemplate.execute((RetryCallback<Boolean, Throwable>) retryContext ->
                        captureMemoryTask(advisedRequest, response)
                );
            } catch (Throwable e) {
                log.error("Memory capture failed", e);
            } finally {
                SecurityContextHolder.clearContext(); // Prevent context leak
            }
        };
    }

    private boolean captureMemoryTask(AdvisedRequest advisedRequest,
                                      AdvisedResponse advisedResponse){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Security context in virtual thread: {}", auth);

        log.info("Initiating captureMemoryTask");
        var retrievedUserContent = basicMemoryExtractor.extractRequestContext(advisedRequest);
        var retrievedAssistantContent = basicMemoryExtractor.extractResponseContext(advisedResponse);
        String chatId = (String) advisedRequest.adviseContext().get(CHAT_MEMORY_CONVERSATION_ID_KEY);
        var capturedMemory = chatClient.prompt()
                .system(promptSystemSpec -> promptSystemSpec.param(
                        "memory", pgChatMemory.get(chatId, 10).stream()
                                .map(Content::getContent)
                                .collect(Collectors.joining(","))))
                .user(retrievedUserContent)
                .call()
                .entity(MemoryLLMResponse.class);

        boolean isUseful = capturedMemory != null && capturedMemory.isUseful();
        if (isUseful) {
            String userName = userService.getLoggedInUser().getUserName();
            log.info("Captured memory: {}, chatId: {}", capturedMemory, chatId);
            vectorStore.accept(
                    List.of(new Document("""
                            Remember this about user:
                            %s
                            """.formatted(capturedMemory.getContent()),
                            Map.of(
                                    "chatId", chatId,
                                    "userName", userName
                            )))
            );
        }

        return isUseful;
    }

    @Override
    public String getName() {
        return "capture-memory";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
