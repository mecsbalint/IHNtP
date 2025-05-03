package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.model.Publisher;

import java.util.List;

public record PublisherForGameDto(long id, String name) {
    public PublisherForGameDto(Publisher publisherEntity) {
        this(
                publisherEntity.getId(),
                publisherEntity.getName()
        );
    }
}
