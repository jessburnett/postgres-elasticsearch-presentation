package com.franglen.service;

import java.util.Arrays;
import java.util.Collection;

import org.dozer.Mapper;
import org.springframework.integration.annotation.Splitter;

import com.franglen.dto.ElasticsearchDocument;
import com.franglen.dto.PostgresDocument;
import com.franglen.dto.WikipediaDocument;

public class DocumentMapper {

    private final Mapper mapper;

    public DocumentMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    @Splitter
    public Collection<Object> split(WikipediaDocument entry) {
        return Arrays.asList(
                mapper.map(entry, ElasticsearchDocument.class),
                mapper.map(entry, PostgresDocument.class)
            );
    }

}
