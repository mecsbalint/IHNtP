package com.mecsbalint.backend.controller.dto.developer;

import com.mecsbalint.backend.model.Developer;

public record DeveloperIdNameDto(long id, String name) {
    public DeveloperIdNameDto(Developer developerEntity) {
        this(
                developerEntity.getId(),
                developerEntity.getName()
        );
    }
}
