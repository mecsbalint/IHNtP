package com.mecsbalint.backend.controller.dto.publisher;

import com.mecsbalint.backend.model.Publisher;

public record PublisherToAdd(String name){
    public PublisherToAdd(Publisher publisher) {
        this(
                publisher.getName()
        );
    }
}
