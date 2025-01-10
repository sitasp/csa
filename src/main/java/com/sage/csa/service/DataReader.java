package com.sage.csa.service;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataReader implements DocumentReader {

    private static final Logger log = LoggerFactory.getLogger(DataReader.class);
//    private final VectorStore vectorStore;
//
//    @Value("classpath:/docs/crustdata_dataset.html")
//    private Resource resource;
//
//    public DataReader(VectorStore vectorStore) {
//        this.vectorStore = vectorStore;
//    }

    @Value("${input.directory}")
    private String inputDirectory;

    @Value(("${input.filename.regex}"))
    private String pattern;

    @Override
    @SneakyThrows
    public List<Document> get() {
        List<Document> docList = new ArrayList<>();
        TikaDocumentReader documentReader;

        try{
            ClassPathResource resourceDirectory = new ClassPathResource(inputDirectory);
            Path directoryPath = resourceDirectory.getFile().toPath();

            Files.newDirectoryStream(directoryPath, pattern).forEach(path -> {
                List<Document> documents;
                try {
                    documents = new TikaDocumentReader(new ByteArrayResource(Files.readAllBytes(path))).get()
                            .stream()
                            .peek(
                                    doc -> {
                                        doc.getMetadata().put("source", path.getFileName());
                                        log.info("Reading document: {}", path.getFileName());
                                    }
                            ).toList();
                } catch (Exception e) {
                    log.error("Error reading document: " + path, e);
                    throw new RuntimeException("Error while reading the file : " + path.toUri() + " :: " + e);
                }
                docList.addAll(documents);
            });
        } catch (IOException e) {
            log.error("Error in reading document operation: ", e);
            throw new RuntimeException("Error in reading document operation: ", e);
        }
        return docList;
    }
}
