package com.franglen.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.franglen.dto.ElasticsearchDocument;

public interface ElasticsearchDocumentRepository extends ElasticsearchRepository<ElasticsearchDocument, String> { }
