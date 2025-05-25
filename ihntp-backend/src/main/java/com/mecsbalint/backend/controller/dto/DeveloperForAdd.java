package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.Developer;
import com.mecsbalint.backend.service.DeveloperService;

public record DeveloperForAdd(String name) {
    public DeveloperForAdd(Developer developer) {
        this(
          developer.getName()
        );
    }
}
