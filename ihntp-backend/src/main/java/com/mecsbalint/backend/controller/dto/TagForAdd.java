package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.Tag;

public record TagForAdd(String name) {
    public TagForAdd(Tag tag) {
        this(
                tag.getName()
        );
    }
}
