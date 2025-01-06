package com.sage.csa.controller;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.output.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RestController
public class ChatController {

    @Autowired
    private StreamingChatLanguageModel streamingChatModel;

    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        // Use Flux.create to stream responses to the client
        return Flux.create(sink -> {
            // Create a handler to process the streaming response
            StreamingResponseHandler<AiMessage> handler = new StreamingResponseHandler<>() {
                @Override
                public void onNext(String token) {
                    // Emit each token to the Flux sink
                    sink.next(token);
                }

                @Override
                public void onComplete(Response<AiMessage> response) {
                    // Complete the Flux stream once the response is done
                    sink.complete();
                }

                @Override
                public void onError(Throwable error) {
                    // Propagate errors to the Flux sink
                    sink.error(error);
                }
            };

            // Send the user message to the language model
            streamingChatModel.generate(Collections.singletonList(UserMessage.from(message)), handler);
        });
    }
}
