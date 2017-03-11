package com.franglen.service;

import com.franglen.dto.PostgresDocument;
import com.franglen.repository.PostgresDocumentRepository;

public class PostgresDocumentWriter {

    private final PostgresDocumentRepository repository;

    public PostgresDocumentWriter(PostgresDocumentRepository repository) {
        this.repository = repository;
    }

    public void save(PostgresDocument document) {
        repository.save(document);
    }

}
