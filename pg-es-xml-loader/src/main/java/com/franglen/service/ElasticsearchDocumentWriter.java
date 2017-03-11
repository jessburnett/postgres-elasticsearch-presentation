package com.franglen.service;

import com.franglen.dto.ElasticsearchDocument;
import com.franglen.repository.ElasticsearchDocumentRepository;

public class ElasticsearchDocumentWriter {

    private final ElasticsearchDocumentRepository repository;

    public ElasticsearchDocumentWriter(ElasticsearchDocumentRepository repository) {
        this.repository = repository;
    }

    public void save(ElasticsearchDocument document) {
        repository.save(document);
    }

}
