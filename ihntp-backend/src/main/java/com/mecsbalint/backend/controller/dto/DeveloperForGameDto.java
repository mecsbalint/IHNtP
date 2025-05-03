package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.Developer;
import com.mecsbalint.backend.model.Game;

import java.util.List;

public record DeveloperForGameDto(long id, String name) {
    public DeveloperForGameDto(Developer developerEntity) {
        this(
                developerEntity.getId(),
                developerEntity.getName()
        );
    }
}
