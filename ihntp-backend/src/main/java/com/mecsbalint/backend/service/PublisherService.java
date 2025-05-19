package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.PublisherIdNameDto;
import com.mecsbalint.backend.model.Publisher;
import com.mecsbalint.backend.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<PublisherIdNameDto> getAllPublishers() {
        List<Publisher> publisherEntities = publisherRepository.findAll();

        return publisherEntities.stream()
                .map(PublisherIdNameDto::new)
                .sorted(Comparator.comparing(PublisherIdNameDto::name))
                .toList();
    }
}
