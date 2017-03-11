package com.franglen.dto;

import static com.google.common.base.Preconditions.checkNotNull;

import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "Builder")
@Data
public class WikipediaDocument {

    private final String id;
    private final String title;
    private final String body;

    public WikipediaDocument(String id, String title, String body) {
        this.id = checkNotNull(id);
        this.title = checkNotNull(title);
        this.body = checkNotNull(body);
    }

}
