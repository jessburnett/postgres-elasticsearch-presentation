package com.franglen.dto;

import javax.persistence.Id;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "wikipedia")
public class ElasticSearchDocument {

    @Id private String id;
    private String title;
    private String body;

}
