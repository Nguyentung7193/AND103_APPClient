package com.example.model.categories;

import java.time.Instant;

public class Categories {
//    @JsonProperty("_id")
    private String id;

    private String name;
    private String description;

    public Categories() {
    }

    public Categories( String description, String id, String name) {
        this.description = description;
        this.id = id;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
