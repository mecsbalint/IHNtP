package com.mecsbalint.backend.controller.dto.tag;

import com.mecsbalint.backend.model.Tag;

public record TagIdNameDto(long id, String name) {
    public TagIdNameDto(Tag tagEntity) {
        this(
                tagEntity.getId(),
                tagEntity.getName()
        );
    }
}
