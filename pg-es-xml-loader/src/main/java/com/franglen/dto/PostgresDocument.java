package com.franglen.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table
public class PostgresDocument {

    @Id private String id;
    @Column(nullable = false, columnDefinition = "TEXT") private String title;
    @Column(nullable = false, columnDefinition = "TEXT") private String body;

}
