package com.franglen.dto;

import lombok.Builder;
import lombok.Data;

@Builder(builderClassName = "Builder")
@Data
public class WikipediaDocument {

    private final String id;
    private final String title;
    private final String body;

}
