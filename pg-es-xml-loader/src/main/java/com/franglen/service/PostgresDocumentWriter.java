package com.franglen.service;

import java.util.Collection;

import javax.transaction.Transactional;

import com.franglen.dto.PostgresDocument;
import com.franglen.repository.PostgresDocumentRepository;

public class PostgresDocumentWriter {

    private final PostgresDocumentRepository repository;

    public PostgresDocumentWriter(PostgresDocumentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void save(Collection<PostgresDocument> documents) {
        repository.save(documents);
    }

    @Transactional
    public void save(PostgresDocument document) {
        repository.save(document);
    }

}
