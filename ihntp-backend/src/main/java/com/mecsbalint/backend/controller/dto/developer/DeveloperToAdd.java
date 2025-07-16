package com.mecsbalint.backend.controller.dto.developer;

import com.mecsbalint.backend.model.Developer;

public record DeveloperToAdd(String name) {
    public DeveloperToAdd(Developer developer) {
        this(
          developer.getName()
        );
    }
}
