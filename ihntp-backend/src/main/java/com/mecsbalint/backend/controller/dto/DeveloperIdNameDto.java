package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.Developer;

public record DeveloperIdNameDto(long id, String name) {
    public DeveloperIdNameDto(Developer developerEntity) {
        this(
                developerEntity.getId(),
                developerEntity.getName()
        );
    }
}
