package com.franglen.service;

import java.util.Collection;

import com.franglen.dto.ElasticsearchDocument;
import com.franglen.repository.ElasticsearchDocumentRepository;

public class ElasticsearchDocumentWriter {

    private final ElasticsearchDocumentRepository repository;

    public ElasticsearchDocumentWriter(ElasticsearchDocumentRepository repository) {
        this.repository = repository;
    }

    public void save(Collection<ElasticsearchDocument> documents) {
        repository.save(documents);
    }

    public void save(ElasticsearchDocument document) {
        repository.save(document);
    }

}
