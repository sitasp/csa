package com.sage.csa.controller;

import com.sage.csa.service.EtlPipeline;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IngestionController {

    EtlPipeline etlPipeline;

    public IngestionController(EtlPipeline etlPipeline){
        this.etlPipeline = etlPipeline;
    }

    @PostMapping("/run-ingestion")
    public ResponseEntity<?> run() {
        etlPipeline.runIngestion();
        return ResponseEntity.accepted().build();
    }
}
