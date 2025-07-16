package com.mecsbalint.backend.controller.dto.tag;

import com.mecsbalint.backend.model.Tag;

public record TagToAdd(String name) {
    public TagToAdd(Tag tag) {
        this(
                tag.getName()
        );
    }
}
