package com.sage.csa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class EtlPipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(EtlPipeline.class);

    @Autowired private DataReader dataReader;
    @Autowired private VectorStore vectorStore;
    @Autowired private TextSplitter textSplitter;

    public void runIngestion() {

        LOGGER.info("RunIngestion() started");
        Mono.fromCallable(() -> {
            vectorStore.write(textSplitter.apply(dataReader.get()));    // ETL Pipeline
                    return Mono.empty();
        }).subscribeOn(Schedulers.boundedElastic())
        .subscribe(response -> {
            LOGGER.info("RunIngestion() finished");
        }, error -> {
            LOGGER.error("Error during ingestion", error);
        }, () -> {
            LOGGER.info("RunIngestion() completed");
        });
    }
}
