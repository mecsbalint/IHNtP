package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.publisher.PublisherToAdd;
import com.mecsbalint.backend.controller.dto.publisher.PublisherIdNameDto;
import com.mecsbalint.backend.service.publisher.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {
    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public List<PublisherIdNameDto> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Long> addPublishers(@RequestBody List<PublisherToAdd> publishersToAdd) {
        return publisherService.addPublishers(publishersToAdd);
    }
}
