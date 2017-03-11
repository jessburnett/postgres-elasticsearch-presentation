package com.franglen.repository;

import org.springframework.data.repository.CrudRepository;

import com.franglen.dto.PostgresDocument;

public interface PostgresDocumentRepository extends CrudRepository<PostgresDocument, String> { }
