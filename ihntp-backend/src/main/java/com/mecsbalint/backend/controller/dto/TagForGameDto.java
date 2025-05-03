package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.model.Tag;

import java.util.List;

public record TagForGameDto(long id, String name) {
    public TagForGameDto(Tag tagEntity) {
        this(
                tagEntity.getId(),
                tagEntity.getName()
        );
    }
}
