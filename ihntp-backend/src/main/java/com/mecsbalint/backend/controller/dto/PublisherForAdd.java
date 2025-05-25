package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.Publisher;

public record PublisherForAdd (String name){
    public PublisherForAdd(Publisher publisher) {
        this(
                publisher.getName()
        );
    }
}
