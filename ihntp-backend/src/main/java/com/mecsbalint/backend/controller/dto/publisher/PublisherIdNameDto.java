package com.mecsbalint.backend.controller.dto.publisher;

import com.mecsbalint.backend.model.Publisher;

public record PublisherIdNameDto(long id, String name) {
    public PublisherIdNameDto(Publisher publisherEntity) {
        this(
                publisherEntity.getId(),
                publisherEntity.getName()
        );
    }
}
